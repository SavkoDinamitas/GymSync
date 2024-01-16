package raf.sk.teretanaservis.service.impl;

import io.github.resilience4j.retry.Retry;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import komedija.CekicanjeDto;
import komedija.ManagerCheckDto;
import komedija.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import raf.sk.teretanaservis.domain.NedostupniTermini;
import raf.sk.teretanaservis.domain.Rezervacija;
import raf.sk.teretanaservis.domain.Trening;
import raf.sk.teretanaservis.dto.FreeReservationDto;
import raf.sk.teretanaservis.dto.RezervacijaDto;
import raf.sk.teretanaservis.exception.CustomException;
import raf.sk.teretanaservis.exception.ErrorCode;
import raf.sk.teretanaservis.exception.NotFoundException;
import raf.sk.teretanaservis.repository.NedostupniTerminiRepository;
import raf.sk.teretanaservis.repository.RezervacijaRepository;
import raf.sk.teretanaservis.repository.TreningRepository;
import raf.sk.teretanaservis.security.service.TokenService;
import raf.sk.teretanaservis.service.*;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service@Transactional@AllArgsConstructor
public class CekicanjeServisImpl implements CekicanjeServis {
    private RestTemplate userServiceRestTemplate;
    private TreningService treningService;
    private BanovacaServis banovacaServis;
    private RezervacijaServis rezervacijaServis;
    private TreningRepository treningRepository;
    private TeretaneService teretaneService;
    private RezervacijaRepository rezervacijaRepository;
    private NotificationService notificationService;
    private NedostupniTerminiRepository nedostupniTerminiRepository;
    private TokenService tokenService;

    private final int MAKSIMALNI_BROJ_U_GRUPI = 12;
    private final Retry retryPattern;

    @Override
    public List<LocalTime> slobodniTermini(FreeReservationDto freeReservationDto) {
        List<LocalTime> slobodniTermini = new ArrayList<>();

        for(LocalTime termin = freeReservationDto.getStartTime(); termin.isBefore(freeReservationDto.getEndTime()); termin = termin.plusHours(1)){
            LocalDateTime trenutno = freeReservationDto.getDatum().atTime(termin);
            if(slobodan(new RezervacijaDto(freeReservationDto.getIdUsera(), freeReservationDto.getIdTreninga(), trenutno))){
                slobodniTermini.add(termin);
            }
        }
        return slobodniTermini;
    }

    private boolean slobodan(RezervacijaDto komedija){
        List<LocalDateTime> zauzetiTermini = banovacaServis.dajSveBanovaneTermine();

        if(!zauzetiTermini.contains(komedija.getTermin())){
            return true;
        }
        List<RezervacijaDto> domacin = rezervacijaServis.findAllReservations(komedija.getTreningId(), komedija.getTermin());
        if(!domacin.isEmpty()){
            boolean individualni = treningService.findById(komedija.getTreningId()).isIndividualni();
            if(!individualni){
                boolean flag = false;
                for(var rezervacije : domacin){
                    if(rezervacije.getUserId().equals(komedija.getUserId())){
                        flag = true;
                    }
                }
                if(!flag && domacin.size() < MAKSIMALNI_BROJ_U_GRUPI){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public HttpStatus kreirajTermin(RezervacijaDto rezervacija) {
        if(!slobodan(rezervacija)){
            return HttpStatus.NOT_ACCEPTABLE;
        }

        //Retry pattern za uzimanje podataka od User-a
        CekicanjeDto numOfTraining = Retry.decorateSupplier(retryPattern,
                () -> brojTreninga(rezervacija.getUserId())).get();


        //uvecavanje broja treninga
        userServiceRestTemplate.exchange("/user/" + rezervacija.getUserId(),
                HttpMethod.POST, null, CekicanjeDto.class);
        Optional<Trening> tr = treningRepository.findTreningById(rezervacija.getTreningId());
        if(tr.isEmpty()){
            return HttpStatus.NOT_ACCEPTABLE;
        }
        Rezervacija rez = new Rezervacija();
        rez.setTermin(rezervacija.getTermin());
        rez.setTrening(tr.get());
        rez.setUserId(rezervacija.getUserId());
        rez.setPrice(tr.get().getCena());
        //resavanje popusta
        if(numOfTraining.getBroj_zakazanih_treninga() % teretaneService.getPopust(tr.get().getTeretana().getId()) == 0){
            rez.setPrice(0);
        }
        //slanje mejla
        NotificationDto mejlic = new NotificationDto();
        mejlic.setEmail(numOfTraining.getEmail());
        mejlic.setMessage("Zakazan trening u " + rezervacija.getTermin().toString() + "tip treninga:" + tr.get().getTipTreninga());
        mejlic.setNotification_type("Zakazan trening");
        mejlic.setId_korisnika(rezervacija.getUserId());
        notificationService.notify(mejlic);

        //cuvanje u zauzete termine
        rezervacijaRepository.save(rez);
        var nn = new NedostupniTermini();
        nn.setTermin(rezervacija.getTermin());
        Optional<NedostupniTermini> xd = nedostupniTerminiRepository.findNedostupniTerminiByTermin(rezervacija.getTermin());
        if(xd.isEmpty())
            nedostupniTerminiRepository.save(nn);

        return HttpStatus.CREATED;
    }

    @Override
    public HttpStatus otkaziTermin(Long id, String authorization) {
        Claims koristanDeo = tokenService.parseToken(authorization.split(" ")[1]);
        String role = koristanDeo.get("role", String.class);
        Long userId = koristanDeo.get("id", Long.class);
        Optional<Rezervacija> r = rezervacijaRepository.findRezervacijaById(id);
        if(r.isEmpty()){
            throw new NotFoundException("Ne postoji rezervacija sa id: " + id);
        }

        if((role.equals("MENADZER") && praviMenadzer(authorization, r.get().getTrening().getTeretana().getId())) || role.equals("ADMIN")){
            //menadzersko otkazivanje
            List<Rezervacija> rezervacije = rezervacijaRepository.findAllByTreningAndTermin(r.get().getTrening(), r.get().getTermin());
            for(var res : rezervacije){
                rezervacijaRepository.delete(res);
                //smanji broj rezervacija
                CekicanjeDto volimOvajDto = userServiceRestTemplate.exchange("/user/smanji/" + res.getUserId(),
                        HttpMethod.POST, null, CekicanjeDto.class).getBody();
                NotificationDto mejlic = new NotificationDto();
                mejlic.setEmail(volimOvajDto.getEmail());
                mejlic.setNotification_type("Otkazan trening");
                mejlic.setId_korisnika(res.getUserId());
                mejlic.setMessage("Trening koji ste zakazali je nazalost otkazan \n Vrsta treninga: " +
                        res.getTrening().getTipTreninga() + "\n Vreme: " + res.getTermin());
                notificationService.notify(mejlic);
                //posto je menadzer termin ostaje zauzet
            }
        }
        else{
            //korisnicko otkazivanje
            if(!userId.equals(r.get().getUserId())){
                return HttpStatus.UNAUTHORIZED;
            }
            List<Rezervacija> rezervacije = rezervacijaRepository.findAllByTreningAndTermin(r.get().getTrening(), r.get().getTermin());
            if(rezervacije.size() == 1){
                Optional<NedostupniTermini> xd = nedostupniTerminiRepository.findNedostupniTerminiByTermin(r.get().getTermin());
                nedostupniTerminiRepository.delete(xd.get());
            }
            rezervacijaRepository.delete(r.get());
            //smanji broj rezervacija
            CekicanjeDto volimOvajDto = userServiceRestTemplate.exchange("/user/smanji/" + r.get().getUserId(),
                    HttpMethod.POST, null, CekicanjeDto.class).getBody();
            NotificationDto mejlic = new NotificationDto();
            mejlic.setEmail(volimOvajDto.getEmail());
            mejlic.setNotification_type("Otkazan trening");
            mejlic.setId_korisnika(r.get().getUserId());
            mejlic.setMessage("Uspesno ste otkazali trening \n Vrsta treninga: " +
                    r.get().getTrening().getTipTreninga() + "\n Vreme: " + r.get().getTermin());
            notificationService.notify(mejlic);
        }
        return null;
    }

    private CekicanjeDto brojTreninga(Long id) {
        try {
            return userServiceRestTemplate.exchange("/user/" + id,
                    HttpMethod.GET, null, CekicanjeDto.class).getBody();
        }
        catch (HttpClientErrorException e) {
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new RuntimeException("User with given id doesn't exist in database");

            throw new RuntimeException("Internal server error");
        }
        catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }

    private boolean praviMenadzer(String authorization, Long id){
        Claims mast = tokenService.parseToken(authorization.split(" ")[1]);
        String role = mast.get("role", String.class);
        Long idUsera = mast.get("id", Long.class);
        if(!role.equals("MENADZER"))
            return true;

        ManagerCheckDto mc = userServiceRestTemplate.exchange("/user/sala/" + idUsera,
                HttpMethod.GET, null, ManagerCheckDto.class).getBody();

        if(!id.equals((long)mc.getIdTeretane())){
            return false;
        }
        return true;
    }
}

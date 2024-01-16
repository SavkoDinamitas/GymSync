package raf.sk.teretanaservis.service.impl;

import io.github.resilience4j.retry.Retry;
import jakarta.transaction.Transactional;
import komedija.CekicanjeDto;
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
import raf.sk.teretanaservis.repository.NedostupniTerminiRepository;
import raf.sk.teretanaservis.repository.RezervacijaRepository;
import raf.sk.teretanaservis.repository.TreningRepository;
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
        if(teretaneService.getPopust(tr.get().getTeretana().getId()) == numOfTraining.getBroj_zakazanih_treninga()){
            rez.setPrice(0);
        }
        //slanje mejla
        NotificationDto mejlic = new NotificationDto();
        mejlic.setEmail(numOfTraining.getEmail());
        mejlic.setMessage("Zakazan trening u " + rezervacija.getTermin().toString() + "tip treninga: " + tr.get().getTipTreninga());
        mejlic.setNotification_type("Zakazan trening");
        mejlic.setId_korisnika(rezervacija.getUserId());
        notificationService.notify(mejlic);

        //cuvanje u zauzete termine
        rezervacijaRepository.save(rez);
        var nn = new NedostupniTermini();
        nn.setTermin(rezervacija.getTermin());
        nedostupniTerminiRepository.save(nn);

        return HttpStatus.CREATED;
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
}

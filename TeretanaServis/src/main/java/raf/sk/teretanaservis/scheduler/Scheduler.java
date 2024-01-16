package raf.sk.teretanaservis.scheduler;

import jakarta.transaction.Transactional;
import komedija.CekicanjeDto;
import komedija.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import raf.sk.teretanaservis.domain.Rezervacija;
import raf.sk.teretanaservis.domain.Trening;
import raf.sk.teretanaservis.repository.RezervacijaRepository;
import raf.sk.teretanaservis.service.CekicanjeServis;
import raf.sk.teretanaservis.service.NotificationService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional
@AllArgsConstructor
public class Scheduler {
    private final RezervacijaRepository rezervacijaRepository;
    private final RestTemplate userServiceRestTemplate;
    private final NotificationService notificationService;
    private final CekicanjeServis cekicanjeServis;

    @Scheduled(fixedDelay = 3600000, initialDelay = 10000)
    public void podsetnik() {

        LocalDateTime pocetak = LocalDateTime.now().plusHours(23).plusMinutes(1);
        LocalDateTime kraj = LocalDateTime.now().plusDays(1);

        //otkazivanje grupnih ako nema dovoljno ljudi

        List<Rezervacija> rezervacije = rezervacijaRepository.findAllByTerminBetween(pocetak, kraj);

        Set<Pair<Trening, LocalDateTime>> vremena = new HashSet<>();
        for(var x : rezervacije){
            if(!x.getTrening().isIndividualni())
                vremena.add(Pair.of(x.getTrening(), x.getTermin()));
        }

        for(var ddx : vremena){
            List<Rezervacija> grupni = rezervacijaRepository.findAllByTreningAndTermin(ddx.getFirst(), ddx.getSecond());
            if(grupni.size() < 4){
                cekicanjeServis.otkaziTermin(grupni.get(0).getId(),
                        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6OSwicm9sZSI6IkFETUlOIn0.LTClmsDk2T2QM8PO-cptrO3qWK_-OluEpjkCgYFRrBYtSfeCq5tQnaDr3MyCRTqkInVqJBkQKNqSn_6RsGG9Yw");
            }
        }

        //slanje mejlova 24 sata pred kao podsetnik
        for(Rezervacija res: rezervacije) {
            CekicanjeDto xd = userServiceRestTemplate.exchange("/user/" + res.getUserId(),
                    HttpMethod.GET, null, CekicanjeDto.class).getBody();

            NotificationDto mejlic = new NotificationDto();
            mejlic.setEmail(xd.getEmail());
            mejlic.setNotification_type("Podsetnik za trening");
            mejlic.setId_korisnika(res.getUserId());
            mejlic.setMessage("Podsetnik da imate trening sutra \n Vrsta treninga: " +
                    res.getTrening().getTipTreninga() + "\n Vreme: " + res.getTermin());
            notificationService.notify(mejlic);
        }
    }
}

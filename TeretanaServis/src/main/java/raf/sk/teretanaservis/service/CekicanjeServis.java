package raf.sk.teretanaservis.service;

import org.springframework.http.HttpStatus;
import raf.sk.teretanaservis.dto.FreeReservationDto;
import raf.sk.teretanaservis.dto.RezervacijaDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface CekicanjeServis {
    List<LocalTime> slobodniTermini(FreeReservationDto freeReservationDto);

    HttpStatus kreirajTermin(RezervacijaDto rezervacija);
}

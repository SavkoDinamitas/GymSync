package raf.sk.teretanaservis.service;

import raf.sk.teretanaservis.dto.RezervacijaDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RezervacijaServis {
    List<RezervacijaDto> findAllReservations(Long idTreninga, LocalDateTime termin);

}

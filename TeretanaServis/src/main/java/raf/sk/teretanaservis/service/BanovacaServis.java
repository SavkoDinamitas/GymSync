package raf.sk.teretanaservis.service;

import raf.sk.teretanaservis.dto.RezervacijaDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BanovacaServis {
    List<LocalDateTime> dajSveBanovaneTermine();
}

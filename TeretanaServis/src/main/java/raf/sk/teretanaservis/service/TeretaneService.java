package raf.sk.teretanaservis.service;

import raf.sk.teretanaservis.domain.Teretana;
import raf.sk.teretanaservis.dto.TeretanaDto;

import java.util.List;

public interface TeretaneService {
    TeretanaDto add(TeretanaDto teretana);

    TeretanaDto delete(Long id);

    TeretanaDto put(TeretanaDto teretanaDto, Long id);

    List<TeretanaDto> findAll();

    int getPopust(Long id);
}

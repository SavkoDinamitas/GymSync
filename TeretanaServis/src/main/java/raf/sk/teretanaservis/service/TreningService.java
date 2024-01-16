package raf.sk.teretanaservis.service;

import raf.sk.teretanaservis.dto.TeretanaDto;
import raf.sk.teretanaservis.dto.TreningDto;

import java.util.List;

public interface TreningService {
    TreningDto add(TreningDto teretana, String authorization);

    TreningDto delete(Long id, String authorization);

    TreningDto put(TreningDto teretanaDto, Long id, String authorization);

    List<TreningDto> findAll();

    TreningDto findById(Long id);

    List<TreningDto> findTreningForTeretana(Long id);
}

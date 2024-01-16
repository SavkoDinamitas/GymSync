package raf.sk.teretanaservis.service;

import raf.sk.teretanaservis.dto.TeretanaDto;
import raf.sk.teretanaservis.dto.TreningDto;

import java.util.List;

public interface TreningService {
    TreningDto add(TreningDto teretana);

    TreningDto delete(Long id);

    TreningDto put(TreningDto teretanaDto, Long id);

    List<TreningDto> findAll();

    TreningDto findById(Long id);

    List<TreningDto> findTreningForTeretana(Long id);
}

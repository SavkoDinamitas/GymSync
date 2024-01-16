package raf.sk.teretanaservis.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raf.sk.teretanaservis.domain.Rezervacija;
import raf.sk.teretanaservis.domain.Trening;
import raf.sk.teretanaservis.dto.RezervacijaDto;
import raf.sk.teretanaservis.exception.NotFoundException;
import raf.sk.teretanaservis.mapper.TeretanaMapper;
import raf.sk.teretanaservis.repository.RezervacijaRepository;
import raf.sk.teretanaservis.repository.TreningRepository;
import raf.sk.teretanaservis.service.RezervacijaServis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service@Transactional@AllArgsConstructor
public class RezervacijaServisImpl implements RezervacijaServis {
    private RezervacijaRepository rezervacijaRepository;
    private TreningRepository treningRepository;
    private TeretanaMapper teretanaMapper;
    @Override
    public List<RezervacijaDto> findAllReservations(Long idTreninga, LocalDateTime termin) {
        Optional<Trening> xd = treningRepository.findTreningById(idTreninga);
        if(xd.isEmpty()){
            throw new NotFoundException("Nema treninga sa id: " + idTreninga);
        }
        return rezervacijaRepository.findAllByTreningAndTermin(xd.get(), termin).stream().map(teretanaMapper::RezervacijaToDto).toList();
    }
}

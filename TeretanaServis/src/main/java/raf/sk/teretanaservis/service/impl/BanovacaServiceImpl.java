package raf.sk.teretanaservis.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raf.sk.teretanaservis.domain.NedostupniTermini;
import raf.sk.teretanaservis.dto.RezervacijaDto;
import raf.sk.teretanaservis.repository.NedostupniTerminiRepository;
import raf.sk.teretanaservis.service.BanovacaServis;

import java.time.LocalDateTime;
import java.util.List;
@Service@Transactional@AllArgsConstructor
public class BanovacaServiceImpl implements BanovacaServis {

    private NedostupniTerminiRepository nedostupniTerminiRepository;
    @Override
    public List<LocalDateTime> dajSveBanovaneTermine() {
        return nedostupniTerminiRepository.findAll().stream().map(NedostupniTermini::getTermin).toList();
    }
}

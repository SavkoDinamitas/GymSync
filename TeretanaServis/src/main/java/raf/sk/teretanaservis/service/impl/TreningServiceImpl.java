package raf.sk.teretanaservis.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import raf.sk.teretanaservis.domain.Teretana;
import raf.sk.teretanaservis.domain.Trening;
import raf.sk.teretanaservis.dto.TreningDto;
import raf.sk.teretanaservis.exception.NotFoundException;
import raf.sk.teretanaservis.mapper.TeretanaMapper;
import raf.sk.teretanaservis.repository.TeretanaRepository;
import raf.sk.teretanaservis.repository.TreningRepository;
import raf.sk.teretanaservis.service.TreningService;

import java.util.List;
import java.util.Optional;

@Service@Transactional
@Data@AllArgsConstructor
public class TreningServiceImpl implements TreningService {
    private TreningRepository treningRepository;
    private TeretanaRepository teretanaRepository;
    private TeretanaMapper teretanaMapper;
    @Override
    public TreningDto add(TreningDto teretana) {
        Trening xd = teretanaMapper.DtoToTrening(teretana);
        treningRepository.save(xd);
        return teretana;
    }

    @Override
    public TreningDto delete(Long id) {
        Optional<Trening> xd = treningRepository.findTreningById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Teretana sa id-jem: " + id + " ne postoji");
        }
        treningRepository.delete(xd.get());
        return teretanaMapper.TreningToDto(xd.get());
    }

    @Override
    public TreningDto put(TreningDto teretanaDto, Long id) {
        Optional<Trening> xd = treningRepository.findTreningById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Teretana sa id-jem: " + id + " ne postoji");
        }
        teretanaMapper.putTrening(xd.get(), teretanaDto);
        Optional<Teretana> slavko = teretanaRepository.findTeretanaById(teretanaDto.getTeretanaId());
        if(slavko.isPresent()){
            xd.get().setTeretana(slavko.get());
        }
        treningRepository.save(xd.get());
        return teretanaMapper.TreningToDto(xd.get());
    }

    @Override
    public List<TreningDto> findAll() {
        return treningRepository.findAll().stream().map(teretanaMapper::TreningToDto).toList();
    }

    @Override
    public TreningDto findById(Long id) {
        Optional<Trening> xd = treningRepository.findTreningById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Nema treninga sa id: " + id);
        }
        return teretanaMapper.TreningToDto(xd.get());
    }

    @Override
    public List<TreningDto> findTreningForTeretana(Long id) {
        Optional<Teretana> xd = teretanaRepository.findTeretanaById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji teretana sa id: " + id);
        }
        return treningRepository.findAllByTeretana(xd.get()).stream().map(teretanaMapper::TreningToDto).toList();
    }
}

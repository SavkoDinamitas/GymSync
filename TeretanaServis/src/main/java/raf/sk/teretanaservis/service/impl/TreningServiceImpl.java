package raf.sk.teretanaservis.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import komedija.ManagerCheckDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import raf.sk.teretanaservis.domain.Teretana;
import raf.sk.teretanaservis.domain.Trening;
import raf.sk.teretanaservis.dto.TreningDto;
import raf.sk.teretanaservis.exception.CustomException;
import raf.sk.teretanaservis.exception.ErrorCode;
import raf.sk.teretanaservis.exception.NotFoundException;
import raf.sk.teretanaservis.mapper.TeretanaMapper;
import raf.sk.teretanaservis.repository.TeretanaRepository;
import raf.sk.teretanaservis.repository.TreningRepository;
import raf.sk.teretanaservis.security.service.TokenService;
import raf.sk.teretanaservis.service.TreningService;

import java.util.List;
import java.util.Optional;

@Service@Transactional
@Data@AllArgsConstructor
public class TreningServiceImpl implements TreningService {
    private TreningRepository treningRepository;
    private TeretanaRepository teretanaRepository;
    private TeretanaMapper teretanaMapper;
    private TokenService tokenService;
    private RestTemplate userServiceRestTemplate;
    @Override
    public TreningDto add(TreningDto teretana, String authorization) {
        praviMenadzer(authorization, teretana.getTeretanaId());
        Trening xd = teretanaMapper.DtoToTrening(teretana);
        Optional<Teretana> tt = teretanaRepository.findTeretanaById(teretana.getTeretanaId());
        if(tt.isEmpty()){
            throw new NotFoundException("Teretana sa id-jem: " + teretana.getTeretanaId() + " ne postoji");
        }
        xd.setTeretana(tt.get());
        treningRepository.save(xd);
        return teretana;
    }

    @Override
    public TreningDto delete(Long id, String authorization) {
        Optional<Trening> xd = treningRepository.findTreningById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Trening sa id-jem: " + id + " ne postoji");
        }
        praviMenadzer(authorization, xd.get().getTeretana().getId());
        treningRepository.delete(xd.get());
        return teretanaMapper.TreningToDto(xd.get());
    }

    @Override
    public TreningDto put(TreningDto teretanaDto, Long id, String authorization) {
        Optional<Trening> xd = treningRepository.findTreningById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Trening sa id-jem: " + id + " ne postoji");
        }
        praviMenadzer(authorization, xd.get().getTeretana().getId());
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

    private void praviMenadzer(String authorization, Long id){
        Claims mast = tokenService.parseToken(authorization.split(" ")[1]);
        String role = mast.get("role", String.class);
        Long idUsera = mast.get("id", Long.class);
        if(!role.equals("MENADZER"))
            return;

        ManagerCheckDto mc = userServiceRestTemplate.exchange("/user/sala/" + idUsera,
                HttpMethod.GET, null, ManagerCheckDto.class).getBody();

        if(!id.equals((long)mc.getIdTeretane())){
            throw new CustomException("Nije menadzer ove sale", ErrorCode.BANNED_FROM_SERVER, HttpStatus.FORBIDDEN);
        }
    }
}

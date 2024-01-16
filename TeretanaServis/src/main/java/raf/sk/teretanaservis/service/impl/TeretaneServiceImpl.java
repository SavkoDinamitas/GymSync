package raf.sk.teretanaservis.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import komedija.CekicanjeDto;
import komedija.ManagerCheckDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import raf.sk.teretanaservis.domain.Teretana;
import raf.sk.teretanaservis.dto.TeretanaDto;
import raf.sk.teretanaservis.exception.CustomException;
import raf.sk.teretanaservis.exception.ErrorCode;
import raf.sk.teretanaservis.exception.NotFoundException;
import raf.sk.teretanaservis.mapper.TeretanaMapper;
import raf.sk.teretanaservis.repository.TeretanaRepository;
import raf.sk.teretanaservis.security.service.TokenService;
import raf.sk.teretanaservis.service.TeretaneService;

import java.util.List;
import java.util.Optional;

@Service@Transactional
@AllArgsConstructor@Data
public class TeretaneServiceImpl implements TeretaneService {
    private TeretanaRepository teretanaRepository;
    private TeretanaMapper teretanaMapper;
    private TokenService tokenService;
    private RestTemplate userServiceRestTemplate;

    @Override
    public TeretanaDto add(TeretanaDto teretana) {
        Teretana xd = teretanaMapper.DtoToTeretana(teretana);
        teretanaRepository.save(xd);
        return teretana;
    }

    @Override
    public TeretanaDto delete(Long id) {
        Optional<Teretana> xd = teretanaRepository.findTeretanaById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Teretana sa id-jem: " + id + " ne postoji");
        }
        teretanaRepository.delete(xd.get());
        return teretanaMapper.TeretanaToDto(xd.get());
    }

    @Override
    public TeretanaDto put(TeretanaDto teretanaDto, Long id, String authorization) {
        Claims mast = tokenService.parseToken(authorization.split(" ")[1]);
        String role = mast.get("role", String.class);
        Long idUsera = mast.get("id", Long.class);
        if(role.equals("MENADZER")){
            ManagerCheckDto mc = userServiceRestTemplate.exchange("/user/sala/" + idUsera,
                    HttpMethod.GET, null, ManagerCheckDto.class).getBody();

            if(!id.equals((long)mc.getIdTeretane())){
                throw new CustomException("Nije menadzer ove sale", ErrorCode.BANNED_FROM_SERVER, HttpStatus.FORBIDDEN);
            }
        }


        Optional<Teretana> xd = teretanaRepository.findTeretanaById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Teretana sa id-jem: " + id + " ne postoji");
        }
        teretanaMapper.putTeretana(xd.get(), teretanaDto);
        teretanaRepository.save(xd.get());
        return teretanaMapper.TeretanaToDto(xd.get());
    }

    @Override
    public List<TeretanaDto> findAll() {
        return teretanaRepository.findAll().stream().map(teretanaMapper::TeretanaToDto).toList();
    }

    @Override
    public int getPopust(Long id) {
        return teretanaRepository.findTeretanaById(id).get().getPopust();
    }
}

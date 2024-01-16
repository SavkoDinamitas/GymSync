package raf.sk.userservice.service;


import komedija.CekicanjeDto;
import komedija.ManagerCheckDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.sk.userservice.domain.User;
import raf.sk.userservice.dto.*;

import javax.swing.text.Utilities;
import java.util.List;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);

    UserDto addManager(ManagerCreateDto userCreateDto);

    UserDto addUser(UserCreateDto userCreateDto);

    BanovacaDto banuj(BanovacaDto banovacaDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    UserDto putUser(Long id, MasanDto gazenje);

    UserDto findById(Long id);

    CekicanjeDto lolcina(Long id);

    CekicanjeDto uvecaj(Long id);

    CekicanjeDto smanji(Long id);

    ManagerCheckDto dajSalu(Long id);
}

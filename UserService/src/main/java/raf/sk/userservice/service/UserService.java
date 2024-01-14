package raf.sk.userservice.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.sk.userservice.dto.*;

import java.util.List;

public interface UserService {
    Page<UserDto> findAll(Pageable pageable);

    UserDto addManager(ManagerCreateDto userCreateDto);

    UserDto addUser(UserCreateDto userCreateDto);

    BanovacaDto banuj(BanovacaDto banovacaDto);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    UserDto putUser(Long id, MasanDto gazenje);
}

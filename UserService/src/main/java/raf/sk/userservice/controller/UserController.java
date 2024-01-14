package raf.sk.userservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.sk.userservice.dto.*;
import raf.sk.userservice.security.CheckSecurity;
import raf.sk.userservice.service.UserService;

import javax.validation.Valid;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/user")
    @CheckSecurity(roles = {"ADMIN", "KLIJENT", "MENADZER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {

        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping("/ticulica")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<BanovacaDto> banovaca(@RequestHeader("Authorization") String authorization, @RequestBody @Valid BanovacaDto banovacaDto){
        return new ResponseEntity<>(userService.banuj(banovacaDto), HttpStatus.OK);
    }
    @PostMapping("/user")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.addUser(userCreateDto), HttpStatus.CREATED);
    }

    @PostMapping("/manager")
    public ResponseEntity<UserDto> saveManager(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        return new ResponseEntity<>(userService.addManager(managerCreateDto), HttpStatus.CREATED);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> changeUser(@RequestBody @Valid MasanDto masanDto, @PathVariable Long id){
        return new ResponseEntity<>(userService.putUser(id, masanDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }
}

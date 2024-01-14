package raf.sk.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raf.sk.userservice.domain.Role;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String ime;
    private String email;
    private String prezime;
    private Role tip;
    private LocalDate datum_rodjenja;
}

package raf.sk.userservice.dto;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter@Setter
public class MasanDto {
    @NotBlank
    private String username;
    @Length(min = 8, max = 20)
    private String password;
    @Email
    private String email;
    @CreatedDate
    private LocalDate datum_rodjenja;
    @NotBlank
    private String ime;
    @NotBlank
    private String prezime;
    @CreatedDate
    private LocalDate datum_zaposljavanja;
    @NotBlank
    private Integer id_sale;
}

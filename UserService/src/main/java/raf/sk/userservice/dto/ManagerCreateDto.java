package raf.sk.userservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter@Setter
public class ManagerCreateDto {
    @Email
    private String email;
    @NotBlank
    private String ime;
    @NotBlank
    private String prezime;
    @NotBlank
    private String username;
    @Length(min = 8, max = 20)
    private String password;
    @CreatedDate
    private LocalDate datum_zaposljavanja;
    @CreatedDate
    private LocalDate datum_rodjenja;
    @NotBlank
    private int id_sale;
}

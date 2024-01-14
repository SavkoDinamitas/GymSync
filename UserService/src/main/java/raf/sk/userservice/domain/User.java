package raf.sk.userservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private LocalDate datum_rodjenja;
    private String ime;
    private String prezime;
    private Role tip;
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private Long broj_clanske_karte;
    private LocalDate datum_zaposljavanja;
    private Integer id_sale;
    private boolean banovaca;
    private int broj_zakazanih_treninga;

    public User(){
        this.banovaca = false;
        this.broj_zakazanih_treninga = 0;
    }
}

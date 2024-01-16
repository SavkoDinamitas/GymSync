package raf.sk.teretanaservis.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity@Data@NoArgsConstructor@AllArgsConstructor@Table(name = "teretane")
public class Teretana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String naziv;
    @Column(length = 100)
    private String opis;
    @Column(nullable = false)
    private int brojTrenera;
    @Column(nullable = false)
    private int popust;
}

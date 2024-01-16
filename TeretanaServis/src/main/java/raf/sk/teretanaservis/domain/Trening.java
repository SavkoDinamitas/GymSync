package raf.sk.teretanaservis.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity@Data@NoArgsConstructor@AllArgsConstructor@Table(name = "treninzi")
public class Trening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String tipTreninga;

    private boolean individualni;

    private double cena;
    @ManyToOne(optional = false)
    private Teretana teretana;
}

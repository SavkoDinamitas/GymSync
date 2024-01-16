package raf.sk.teretanaservis.dto;

import jakarta.persistence.ManyToOne;
import lombok.Data;
import raf.sk.teretanaservis.domain.Teretana;
@Data
public class TreningDto {
    private String tipTreninga;

    private boolean individualni;

    private double cena;
    private Long teretanaId;
}

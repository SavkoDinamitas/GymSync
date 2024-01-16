package raf.sk.teretanaservis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class TeretanaDto {
    private String naziv;
    private String opis;
    private int brojTrenera;
    private int popust;
}

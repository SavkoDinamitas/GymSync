package raf.sk.teretanaservis.dto;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raf.sk.teretanaservis.domain.Trening;

import java.time.LocalDateTime;
@Data@NoArgsConstructor@AllArgsConstructor
public class RezervacijaDto{
    private Long userId;
    private Long treningId;
    private LocalDateTime termin;
}

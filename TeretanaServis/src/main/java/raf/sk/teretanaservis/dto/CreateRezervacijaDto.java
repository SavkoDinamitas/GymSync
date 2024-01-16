package raf.sk.teretanaservis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data@AllArgsConstructor@NoArgsConstructor
public class CreateRezervacijaDto {
    private LocalDateTime termin;
    private Long userId;
    private Long trainingId;
}

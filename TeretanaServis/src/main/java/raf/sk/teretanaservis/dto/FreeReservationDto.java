package raf.sk.teretanaservis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data@NoArgsConstructor@AllArgsConstructor
public class FreeReservationDto {
    private LocalDate datum;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long idTreninga;
    private Long idUsera;
}

package raf.sk.teretanaservis.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.sk.teretanaservis.dto.FreeReservationDto;
import raf.sk.teretanaservis.dto.RezervacijaDto;
import raf.sk.teretanaservis.dto.TeretanaDto;
import raf.sk.teretanaservis.security.CheckSecurity;
import raf.sk.teretanaservis.service.CekicanjeServis;

import java.time.LocalTime;
import java.util.List;

@RestController@AllArgsConstructor@RequestMapping("/cekicanje")
public class CekicanjeController{
    private CekicanjeServis cekicanjeServis;

    @GetMapping()
    public ResponseEntity<List<LocalTime>> getSlobodniTermini(@RequestHeader("Authorization") String authorization, @RequestBody FreeReservationDto komedija) {

        return new ResponseEntity<>(cekicanjeServis.slobodniTermini(komedija), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> zakaziTermin(@RequestHeader("Authorization") String authorization, @RequestBody RezervacijaDto komedija) {

        return new ResponseEntity<>(cekicanjeServis.kreirajTermin(komedija), HttpStatus.OK);
    }
}

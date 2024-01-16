package raf.sk.teretanaservis.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.sk.teretanaservis.dto.TreningDto;
import raf.sk.teretanaservis.security.CheckSecurity;
import raf.sk.teretanaservis.service.TreningService;

import javax.validation.Valid;
import java.util.List;

@RestController@RequestMapping("/trening")@AllArgsConstructor
public class TreningController {
    private TreningService treningService;

    @GetMapping()
    @CheckSecurity(roles = {"ADMIN", "MENADZER"})
    public ResponseEntity<List<TreningDto>> getAllTypes(@RequestHeader("Authorization") String authorization) {

        return new ResponseEntity<>(treningService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    @CheckSecurity(roles = {"ADMIN", "MENADZER"})
    public ResponseEntity<TreningDto> saveType(@RequestHeader("Authorization") String authorization,
                                                        @RequestBody @Valid TreningDto typeDto) {
        return new ResponseEntity<>(treningService.add(typeDto, authorization), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN", "MENADZER"})
    public ResponseEntity<TreningDto> changeType(@RequestHeader("Authorization") String authorization,
                                                          @RequestBody @Valid TreningDto masanDto, @PathVariable Long id){
        return new ResponseEntity<>(treningService.put(masanDto, id, authorization), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN", "MENADZER"})
    public ResponseEntity<TreningDto> deleteType(@RequestHeader("Authorization") String authorization,
                                                          @PathVariable Long id){
        return new ResponseEntity<>(treningService.delete(id, authorization), HttpStatus.OK);
    }
}

package raf.sk.teretanaservis.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.sk.teretanaservis.dto.TeretanaDto;
import raf.sk.teretanaservis.security.CheckSecurity;
import raf.sk.teretanaservis.service.TeretaneService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teretana")@AllArgsConstructor
public class TeretanaController {
    private TeretaneService teretaneService;

    @GetMapping()
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<List<TeretanaDto>> getAllTypes(@RequestHeader("Authorization") String authorization) {

        return new ResponseEntity<>(teretaneService.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<TeretanaDto> saveType(@RequestHeader("Authorization") String authorization,
                                                        @RequestBody @Valid TeretanaDto typeDto) {
        return new ResponseEntity<>(teretaneService.add(typeDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<TeretanaDto> changeType(@RequestHeader("Authorization") String authorization,
                                                          @RequestBody @Valid TeretanaDto masanDto, @PathVariable Long id){
        return new ResponseEntity<>(teretaneService.put(masanDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<TeretanaDto> deleteType(@RequestHeader("Authorization") String authorization,
                                                          @PathVariable Long id){
        return new ResponseEntity<>(teretaneService.delete(id), HttpStatus.OK);
    }
}

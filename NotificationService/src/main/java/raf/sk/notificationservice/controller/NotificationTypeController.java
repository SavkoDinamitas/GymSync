package raf.sk.notificationservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import raf.sk.notificationservice.domain.NotificationType;
import raf.sk.notificationservice.dto.NotificationTypeDto;
import raf.sk.notificationservice.security.CheckSecurity;
import raf.sk.notificationservice.service.NotificationTypeService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/notificationType")
public class NotificationTypeController {

    private NotificationTypeService notificationTypeService;

    @GetMapping()
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<Page<NotificationTypeDto>> getAllTypes(@RequestHeader("Authorization") String authorization,
                                                                 Pageable pageable) {

        return new ResponseEntity<>(notificationTypeService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping()
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<NotificationTypeDto> saveType(@RequestHeader("Authorization") String authorization,
                                                        @RequestBody @Valid NotificationTypeDto typeDto) {
        return new ResponseEntity<>(notificationTypeService.addType(typeDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<NotificationTypeDto> changeType(@RequestHeader("Authorization") String authorization,
                                                          @RequestBody @Valid NotificationTypeDto masanDto, @PathVariable Long id){
        return new ResponseEntity<>(notificationTypeService.putType(id, masanDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ADMIN"})
    public ResponseEntity<NotificationTypeDto> deleteType(@RequestHeader("Authorization") String authorization,
                                                          @PathVariable Long id){
        return new ResponseEntity<>(notificationTypeService.deleteType(id), HttpStatus.OK);
    }
}

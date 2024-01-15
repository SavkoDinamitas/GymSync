package raf.sk.notificationservice.controller;

import komedija.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.sk.notificationservice.service.NotificationService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<NotificationDto>> getAllTypes(@RequestHeader("Authorization") String authorization) {

        return new ResponseEntity<>(notificationService.showNotifications(authorization), HttpStatus.OK);
    }
}

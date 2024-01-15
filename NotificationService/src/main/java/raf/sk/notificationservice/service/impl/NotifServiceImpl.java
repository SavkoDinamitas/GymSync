package raf.sk.notificationservice.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import komedija.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import raf.sk.notificationservice.mapper.NotificationMapper;
import raf.sk.notificationservice.repository.NotificationRepository;
import raf.sk.notificationservice.security.service.TokenService;
import raf.sk.notificationservice.service.NotificationService;

import java.util.List;
@Service@Transactional@AllArgsConstructor
public class NotifServiceImpl implements NotificationService {

    private TokenService tokenService;
    private NotificationRepository notificationRepository;
    private NotificationMapper notificationMapper;
    @Override
    public List<NotificationDto> showNotifications(String authorization) {
        String[] lolcina = authorization.split(" ");
        Claims xd = tokenService.parseToken(lolcina[1]);
        if(xd.get("role", String.class).equals("ADMIN")){
            return notificationRepository.findAll().stream().map(notificationMapper::NotificationToDto).toList();
        }
        else{
            return notificationRepository.findAllById_korisnika(xd.get("id", Long.class)).stream().map(notificationMapper::NotificationToDto).toList();
        }
    }
}

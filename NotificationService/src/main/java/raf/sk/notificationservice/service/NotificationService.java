package raf.sk.notificationservice.service;

import komedija.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> showNotifications(String auth);
}

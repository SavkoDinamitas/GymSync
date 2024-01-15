package raf.sk.notificationservice.service;

import komedija.NotificationDto;

public interface JMSservice {
    void notify(NotificationDto notification);
}

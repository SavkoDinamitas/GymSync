package raf.sk.notificationservice.service;

import jakarta.mail.MessagingException;
import komedija.NotificationDto;

import javax.validation.constraints.NotNull;

public interface MailServis {
    void sendMail(@NotNull NotificationDto porukica) throws MessagingException;
}

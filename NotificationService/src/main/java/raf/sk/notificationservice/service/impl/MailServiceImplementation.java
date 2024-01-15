package raf.sk.notificationservice.service.impl;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import komedija.NotificationDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import raf.sk.notificationservice.domain.Notification;
import raf.sk.notificationservice.domain.NotificationType;
import raf.sk.notificationservice.exception.NotFoundException;
import raf.sk.notificationservice.repository.NotificationRepository;
import raf.sk.notificationservice.repository.NotificationTypeRepository;
import raf.sk.notificationservice.service.MailServis;

import java.util.Optional;

@AllArgsConstructor@Component
public class MailServiceImplementation implements MailServis{
    private JavaMailSender mailSender;
    private NotificationRepository notificationRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImplementation.class);
    @Override@JmsListener(destination = "usro.se.i.umro")
    public void sendMail(NotificationDto porukica) throws MessagingException {
        LOGGER.info("recieved {}", porukica);
        Optional<NotificationType> xdd = notificationTypeRepository.findNotificationTypeByType(porukica.getNotification_type());
        if(xdd.isEmpty()){
            throw new NotFoundException("Nije moguce naci tip notifikacije: " + porukica.getNotification_type());
        }
        Notification ddx = new Notification();
        ddx.setCreatedDateTime(porukica.getCreatedDateTime());
        ddx.setEmail(porukica.getEmail());
        ddx.setMessage(porukica.getMessage());
        ddx.setNotification_type(xdd.get());
        ddx.setId_korisnika(porukica.getId_korisnika());
        notificationRepository.save(ddx);
        var xd = mailSender.createMimeMessage();
        xd.setSubject(porukica.getNotification_type());
        xd.setText(porukica.getMessage());
        xd.setRecipients(Message.RecipientType.TO, porukica.getEmail());
        mailSender.send(xd);
    }
}

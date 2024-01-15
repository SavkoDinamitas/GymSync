package raf.sk.userservice.service.impl;

import komedija.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import raf.sk.userservice.service.NotificationService;
@Service@AllArgsConstructor
public class NotificationServiceImplementation implements NotificationService {
    private JmsTemplate jmsTemplate;
    @Override
    public void notify(NotificationDto notification) {
        jmsTemplate.convertAndSend("usro.se.i.umro", notification);
    }
}

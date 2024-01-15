package raf.sk.notificationservice.service.impl;

import komedija.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import raf.sk.notificationservice.service.JMSservice;
@Service
@AllArgsConstructor
public class JMSNotificationService implements JMSservice {
    private JmsTemplate jmsTemplate;
    @Override
    public void notify(NotificationDto notification) {
        jmsTemplate.convertAndSend("usro.se.i.umro", notification);
    }
}

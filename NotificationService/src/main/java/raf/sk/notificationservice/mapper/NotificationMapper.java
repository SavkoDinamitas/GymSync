package raf.sk.notificationservice.mapper;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import raf.sk.notificationservice.domain.Notification;
import raf.sk.notificationservice.domain.NotificationType;
import raf.sk.notificationservice.dto.NotificationDto;
import raf.sk.notificationservice.dto.NotificationTypeDto;

@Component
public class NotificationMapper {
    public ModelMapper updateModelMapper(){
        var m = new ModelMapper();
        m.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        m.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return m;
    }

    public void putNotification(Notification n, NotificationDto dto){
        var m = updateModelMapper();
        m.map(n, dto);
    }

    public Notification DtoToNotification(NotificationDto dto){
        var m = new ModelMapper();
        return m.map(dto, Notification.class);
    }

    public NotificationDto NotificationToDto(Notification n){
        var m = new ModelMapper();
        return m.map(n, NotificationDto.class);
    }

    public void putNotificationType(NotificationType n, NotificationTypeDto dto){
        var m = updateModelMapper();
        m.map(dto, n);
    }

    public NotificationType DtoToNotificationType(NotificationTypeDto dto){
        var m = new ModelMapper();
        return m.map(dto, NotificationType.class);
    }

    public NotificationTypeDto NotificationTypeToDto(NotificationType n){
        var m = new ModelMapper();
        return m.map(n, NotificationTypeDto.class);
    }
}

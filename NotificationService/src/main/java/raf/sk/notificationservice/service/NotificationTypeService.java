package raf.sk.notificationservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import raf.sk.notificationservice.dto.NotificationTypeDto;

public interface NotificationTypeService {
    Page<NotificationTypeDto> findAll(Pageable pageable);

    NotificationTypeDto addType(NotificationTypeDto dto);

    NotificationTypeDto putType(Long id, NotificationTypeDto dto);

    NotificationTypeDto deleteType(Long id);
}

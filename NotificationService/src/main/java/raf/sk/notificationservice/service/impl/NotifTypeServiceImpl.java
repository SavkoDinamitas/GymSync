package raf.sk.notificationservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.sk.notificationservice.domain.NotificationType;
import raf.sk.notificationservice.dto.NotificationDto;
import raf.sk.notificationservice.dto.NotificationTypeDto;
import raf.sk.notificationservice.exception.NotFoundException;
import raf.sk.notificationservice.mapper.NotificationMapper;
import raf.sk.notificationservice.repository.NotificationRepository;
import raf.sk.notificationservice.repository.NotificationTypeRepository;
import raf.sk.notificationservice.service.NotificationTypeService;

import java.util.Optional;


@Service@Transactional
@AllArgsConstructor
public class NotifTypeServiceImpl implements NotificationTypeService {
    private NotificationTypeRepository notificationTypeRepository;

    private NotificationMapper notificationMapper;


    @Override
    public Page<NotificationTypeDto> findAll(Pageable pageable) {
        return notificationTypeRepository.findAll(pageable)
                .map(notificationMapper::NotificationTypeToDto);
    }

    @Override
    public NotificationTypeDto addType(NotificationTypeDto dto) {
        NotificationType tip = notificationMapper.DtoToNotificationType(dto);
        notificationTypeRepository.save(tip);
        return dto;
    }

    @Override
    public NotificationTypeDto putType(Long id, NotificationTypeDto dto) {
        Optional<NotificationType> xd = notificationTypeRepository.findNotificationTypeById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji tip sa id: " + id);
        }
        NotificationType majmuncina = xd.get();
        notificationMapper.putNotificationType(majmuncina, dto);
        notificationTypeRepository.save(majmuncina);
        return dto;
    }

    @Override
    public NotificationTypeDto deleteType(Long id) {
        Optional<NotificationType> xd = notificationTypeRepository.findNotificationTypeById(id);
        if(xd.isEmpty()){
            throw new NotFoundException("Ne postoji tip sa id: " + id);
        }
        notificationTypeRepository.delete(xd.get());
        return notificationMapper.NotificationTypeToDto(xd.get());
    }
}

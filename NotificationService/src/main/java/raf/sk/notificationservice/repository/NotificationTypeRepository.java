package raf.sk.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.sk.notificationservice.domain.NotificationType;

import java.util.Optional;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    Optional<NotificationType> findNotificationTypeById(Long id);

    Optional<NotificationType> findNotificationTypeByType(String type);
}

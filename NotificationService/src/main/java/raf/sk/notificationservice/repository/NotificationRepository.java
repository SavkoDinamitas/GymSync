package raf.sk.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.sk.notificationservice.domain.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

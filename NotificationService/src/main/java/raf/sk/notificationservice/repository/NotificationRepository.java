package raf.sk.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import raf.sk.notificationservice.domain.Notification;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select u from Notification u where u.id_korisnika=:id")
    List<Notification> findAllById_korisnika(Long id);
}

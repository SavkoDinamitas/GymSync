package raf.sk.notificationservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String message;

    @ManyToOne(optional = false)
    private NotificationType notification_type;
    @Column(nullable = false)
    private String email;
    @CreatedDate
    private LocalDateTime createdDateTime;

    private Long id_korisnika;
    @PrePersist
    public void prePersist(){
        this.createdDateTime = LocalDateTime.now();
    }
}

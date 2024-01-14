package raf.sk.notificationservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import raf.sk.notificationservice.domain.NotificationType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data@NoArgsConstructor@AllArgsConstructor
public class NotificationDto {
    @NotBlank
    private String message;
    @NotNull
    private NotificationType notification_type;
    @Email
    private String email;
    @CreatedDate
    private LocalDateTime createdDateTime;
}

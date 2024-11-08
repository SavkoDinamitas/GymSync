package raf.sk.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data@NoArgsConstructor@AllArgsConstructor
public class NotificationTypeDto {
    @NotBlank
    private String type;
}

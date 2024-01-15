package komedija;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data@NoArgsConstructor@AllArgsConstructor
public class NotificationDto {
    @NotBlank
    private String message;
    @NotNull
    private String notification_type;
    @Email
    private String email;
    private LocalDateTime createdDateTime;
    @NotBlank
    private Long id_korisnika;
}

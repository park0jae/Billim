package dblab.sharing_flatform.dto.notification.crud.create;


import dblab.sharing_flatform.domain.notification.Notification;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationResponseDto {

    private String content;
    private String notificationType;
    private String receiver;

    public static NotificationResponseDto toDto(Notification notification){
        return new NotificationResponseDto(
                notification.getContent(),
                notification.getNotificationType().toString(),
                notification.getReceiver().getUsername()
        );
    }
}


package dblab.sharing_platform.dto.notification;


import dblab.sharing_platform.domain.notification.Notification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationResponseDto {

    private String content;
    private String notificationType;
    private String receiver;
    private String createdDate;

    public static NotificationResponseDto toDto(Notification notification){
        return new NotificationResponseDto(
                notification.getContent(),
                notification.getNotificationType().toString(),
                notification.getReceiver().getNickname(),
                notification.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss"))
        );
    }
}


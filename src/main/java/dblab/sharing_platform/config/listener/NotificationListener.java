package dblab.sharing_platform.config.listener;

import dblab.sharing_platform.dto.notification.NotificationRequest;
import dblab.sharing_platform.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    @TransactionalEventListener
    @Async
    public void handleNotification(NotificationRequest request){
        notificationService.send(request);
    }
}

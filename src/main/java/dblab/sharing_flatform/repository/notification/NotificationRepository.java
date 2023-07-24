package dblab.sharing_flatform.repository.notification;

import dblab.sharing_flatform.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

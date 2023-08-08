package dblab.sharing_platform.repository.notification;

import dblab.sharing_platform.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

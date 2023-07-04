package dblab.sharing_flatform.repository.message;

import dblab.sharing_flatform.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {


}

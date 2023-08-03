package dblab.sharing_flatform.repository.message;

import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.dto.message.MessageDto;
import dblab.sharing_flatform.dto.message.MessagePagingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QMessageRepository {

    Page<MessageDto> findAllBySendMember(MessagePagingCondition cond);

    Page<MessageDto> findAllByReceiverMember(MessagePagingCondition cond);

}

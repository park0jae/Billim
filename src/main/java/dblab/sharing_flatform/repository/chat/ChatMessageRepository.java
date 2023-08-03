package dblab.sharing_flatform.repository.chat;

import dblab.sharing_flatform.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select cm from ChatMessage cm left join fetch cm.chatRoom cr where cr.id =:roomId")
    List<ChatMessage> findAllByRoomId(@Param("roomId") String id);
}

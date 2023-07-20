package dblab.sharing_flatform.repository.message;

import dblab.sharing_flatform.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m join fetch m.sendMember sm where sm.username =:senderName")
    List<Message> findAllBySendMember(@Param("senderName") String senderName);

    @Query("select m from Message m join fetch m.receiveMember rm where rm.username =:receiverName")
    List<Message> findAllByReceiverMember(@Param("receiverName") String receiverName);

    @Query("select m from Message m " +
            "join fetch m.sendMember sm " +
            "join fetch m.receiveMember rm " +
            "where sm.username = :senderName and rm.username = :receiverName")
    List<Message> findAllBySendAndReceiverMembers(@Param("senderName") String senderName, @Param("receiverName") String receiverName);

}

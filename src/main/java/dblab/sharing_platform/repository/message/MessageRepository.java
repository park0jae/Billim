package dblab.sharing_platform.repository.message;

import dblab.sharing_platform.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long>, QMessageRepository {
    @Query("select m from Message m join fetch m.sendMember sm where sm.id = :senderId and m.post.id = :postId")
    Optional<Message> findBySenderIdAndPostId(@Param("senderId") Long senderId, @Param("postId") Long postId);
}

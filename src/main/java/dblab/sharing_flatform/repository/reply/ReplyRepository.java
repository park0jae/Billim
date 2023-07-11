package dblab.sharing_flatform.repository.reply;

import dblab.sharing_flatform.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r left join fetch r.post where r.post.id =:postId")
    List<Reply> findAllByPostId(@Param("postId") Long postId);
}

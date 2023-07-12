package dblab.sharing_flatform.repository.reply;

import dblab.sharing_flatform.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r " +
            "left join fetch r.parent m " +
            "where r.post.id =:postId order by m.id asc nulls first, r.id asc")
    List<Reply> findAllOrderByParentIdAscNullsFirstByPostId(@Param("postId") Long postId);
}

package dblab.sharing_platform.repository.comment;

import dblab.sharing_platform.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select r from Comment r " +
            "left join fetch r.parent m " +
            "where r.post.id =:postId order by m.id asc nulls first, r.id asc")
    List<Comment> findAllOrderByParentIdAscNullsFirstByPostId(@Param("postId") Long postId);
}

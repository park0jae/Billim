package dblab.sharing_flatform.repository.post;

import dblab.sharing_flatform.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository{

    @Query("select p from Post p join fetch p.member m where m.nickname =:nickname")
    List<Post> findAllWithMemberByNickname(@Param("nickname") String nickname);

}

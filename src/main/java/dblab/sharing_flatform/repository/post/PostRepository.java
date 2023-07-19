package dblab.sharing_flatform.repository.post;

import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.post.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository {

    @Query("select p from Post p join fetch p.member m where m.username =:username")
    List<Post> findAllWithMemberByUsername(@Param("username") String username);

}

package dblab.sharing_flatform.repository.likepost;

import dblab.sharing_flatform.domain.likepost.LikePost;
import dblab.sharing_flatform.repository.post.QPostRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikePostRepository extends JpaRepository<LikePost, Long>, QLikePostRepository {
    void deleteByMemberIdAndPostId(Long memberId, Long postId);

    List<LikePost> findAllByMemberId(Long memberId);
    List<LikePost> findAllByPostId(Long postId);
}

package dblab.sharing_platform.repository.likepost;

import dblab.sharing_platform.dto.post.PostDto;
import dblab.sharing_platform.dto.post.PostPagingCondition;
import org.springframework.data.domain.Page;

public interface QLikePostRepository {

    Page<PostDto> findAllLikesByCurrentUsername(PostPagingCondition cond);
}

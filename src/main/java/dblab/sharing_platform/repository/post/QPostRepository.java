package dblab.sharing_platform.repository.post;

import dblab.sharing_platform.dto.post.PostDto;
import dblab.sharing_platform.dto.post.PostPagingCondition;
import org.springframework.data.domain.Page;


public interface QPostRepository {
    Page<PostDto> findAllByCategoryAndTitle(PostPagingCondition cond);
    Page<PostDto> findAllWithMemberByCurrentUsername(PostPagingCondition cond);
}

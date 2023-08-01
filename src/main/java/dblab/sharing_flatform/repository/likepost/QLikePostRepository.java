package dblab.sharing_flatform.repository.likepost;

import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import org.springframework.data.domain.Page;

public interface QLikePostRepository {

    Page<PostDto> findAllLikesByCurrentUsername(PostPagingCondition cond);
}

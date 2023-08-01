package dblab.sharing_flatform.repository.post;

import dblab.sharing_flatform.dto.post.PostDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import org.springframework.data.domain.Page;


public interface QPostRepository {

    // 게시글 검색 및 페이징 ( Querydsl 사용 )
    // 검색조건 : 카테고리 name + 글 제목 -> Paging
    Page<PostDto> findAllByCategoryAndTitle(PostPagingCondition cond);

    Page<PostDto> findAllWithMemberByCurrentUsername(PostPagingCondition cond);

}

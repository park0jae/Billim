package dblab.sharing_flatform.repository.review;

import dblab.sharing_flatform.dto.review.ReviewDto;
import dblab.sharing_flatform.dto.review.crud.read.request.ReviewPagingCondition;
import org.springframework.data.domain.Page;

public interface QReviewRepository {
    Page<ReviewDto> findAllByUsername(ReviewPagingCondition cond);
}

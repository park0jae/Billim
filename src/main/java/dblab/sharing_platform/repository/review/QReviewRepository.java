package dblab.sharing_platform.repository.review;

import dblab.sharing_platform.dto.review.ReviewDto;
import dblab.sharing_platform.dto.review.ReviewPagingCondition;
import org.springframework.data.domain.Page;

public interface QReviewRepository {

    Page<ReviewDto> findAllByUsername(ReviewPagingCondition cond);

    Page<ReviewDto> findAllReviews(ReviewPagingCondition cond);

    Page<ReviewDto> findAllWithMemberByCurrentUsername(ReviewPagingCondition cond);

}

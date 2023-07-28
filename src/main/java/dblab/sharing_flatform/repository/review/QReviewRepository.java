package dblab.sharing_flatform.repository.review;

import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.dto.review.ReviewDto;
import dblab.sharing_flatform.dto.review.crud.read.request.ReviewPagingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QReviewRepository {

    Page<ReviewDto> findAllByUsername(ReviewPagingCondition cond);

    Page<ReviewDto> findAllReviews(ReviewPagingCondition cond);

    Page<ReviewDto> findAllWithMemberByCurrentUsername(ReviewPagingCondition cond);

}

package dblab.sharing_flatform.dto.review.crud.create;

import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.dto.trade.crud.create.TradeResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewResponseDto {

    private Long id;
    private String content;
    private double starRating;
    private String reviewerMember;

    public static ReviewResponseDto toDto(Review review) {
        if (review != null) {
            return new ReviewResponseDto(
                    review.getId(),
                    review.getContent(),
                    review.getStarRating(),
                    review.getReviewerMember().getUsername());
        } else {
            return null;
        }
    }
}

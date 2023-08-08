package dblab.sharing_platform.dto.review;

import dblab.sharing_platform.domain.review.Review;
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
    private String writer;

    public static ReviewResponseDto toDto(Review review) {
        if (review != null) {
            return new ReviewResponseDto(
                    review.getId(),
                    review.getContent(),
                    review.getWriter().getNickname());
        } else {
            return null;
        }
    }
}

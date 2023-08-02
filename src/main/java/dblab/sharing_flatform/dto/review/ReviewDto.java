package dblab.sharing_flatform.dto.review;

import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.dto.post.PostDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {

    private Long reviewId;
    private String receiver;
    private String writer;
    private String content;

    public static ReviewDto toDto(Review review) {
        return new ReviewDto(review.getId(), review.getMember().getNickname(), review.getWriter().getNickname(), review.getContent());
    }

    public static List<ReviewDto> toDtoList(List<Review> reviews) {
        return reviews.stream().map(r -> ReviewDto.toDto(r)).collect(Collectors.toList());
    }
}

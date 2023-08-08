package dblab.sharing_platform.domain.review;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dblab.sharing_platform.factory.review.ReviewFactory.*;

public class ReviewTest {

    @Test
    @DisplayName("리뷰 생성")
    public void createReviewTest() throws Exception {
        //given
        Review review = createReview();

        //then
        Assertions.assertThat(review.getContent()).isEqualTo("테스트 리뷰입니다.");
    }
}

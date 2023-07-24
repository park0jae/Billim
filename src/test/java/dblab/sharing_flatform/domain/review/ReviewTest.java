package dblab.sharing_flatform.domain.review;

import dblab.sharing_flatform.factory.review.ReviewFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dblab.sharing_flatform.factory.review.ReviewFactory.*;

public class ReviewTest {

    @Test
    @DisplayName("리뷰 생성")
    public void createReviewTest() throws Exception {
        //given
        Review review = createReview();

        //when
        double starRating = review.getStarRating();

        //then
        Assertions.assertThat(starRating).isEqualTo(4.5);
    }
}

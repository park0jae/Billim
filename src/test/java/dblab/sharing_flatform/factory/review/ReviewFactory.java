package dblab.sharing_flatform.factory.review;

import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.factory.member.MemberFactory;

import static dblab.sharing_flatform.factory.member.MemberFactory.*;

public class ReviewFactory {

    public static Review createReview(){
        return new Review("테스트 리뷰 작성", 4.5, createRenderMember(), createBorrowerMember());
    }
}

package dblab.sharing_flatform.factory.review;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.factory.member.MemberFactory;

import static dblab.sharing_flatform.factory.member.MemberFactory.*;

public class ReviewFactory {

    public static Review createReview(){
        return new Review("테스트 리뷰입니다.",  createRenderMember(), createBorrowerMember());
    }
    public static Review createReviewWithMember(Member member , Member reviewerMember){
        return new Review("테스트 리뷰입니다.", member, reviewerMember);
    }
}

package dblab.sharing_flatform.service.review;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.review.ReviewDto;
import dblab.sharing_flatform.dto.review.crud.create.ReviewRequestDto;
import dblab.sharing_flatform.dto.review.crud.create.ReviewResponseDto;
import dblab.sharing_flatform.dto.review.crud.read.request.ReviewPagingCondition;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.review.ExistReviewException;
import dblab.sharing_flatform.exception.review.ImpossibleWriteReviewException;
import dblab.sharing_flatform.exception.review.ReviewNotFoundException;
import dblab.sharing_flatform.exception.trade.TradeNotCompleteException;
import dblab.sharing_flatform.exception.trade.TradeNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.review.ReviewRepository;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;

    @Transactional
    public ReviewResponseDto writeReview(ReviewRequestDto reviewRequestDto, Long tradeId, Long memberId){
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(TradeNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        validate(memberId, trade);

        Review review = new Review(reviewRequestDto.getContent(),
                reviewRequestDto.getStarRating(),
                member,
                memberRepository.findById(trade.getRenderMember().getId()).orElseThrow(MemberNotFoundException::new));

        reviewRepository.save(review);
        trade.addReview(review);

        // member Rating 계산
        member.calculateTotalStarRating(reviewRequestDto.getStarRating(), reviewRepository.countByMemberId(memberId));
        return ReviewResponseDto.toDto(review);
    }


    @Transactional
    public void deleteReview(Long tradeId){
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(TradeNotFoundException::new);
        Review review = reviewRepository.findById(trade.getReview().getId()).orElseThrow(ReviewNotFoundException::new);
        trade.deleteReview();

        reviewRepository.delete(review);
    }

    public List<ReviewResponseDto> findAllReviews(){
        return reviewRepository.findAll().stream().map(review -> ReviewResponseDto.toDto(review)).collect(Collectors.toList());
    }

    public List<ReviewResponseDto> findCurrentUserReviews(Long memberId){
        return reviewRepository.findAllWithMemberByMemberId(memberId).stream().map(review -> ReviewResponseDto.toDto(review)).collect(Collectors.toList());
    }

    public Page<ReviewDto> findAllReviewsByUsername(ReviewPagingCondition cond){
        return reviewRepository.findAllByUsername(cond);
    }

    private void validate(Long memberId, Trade trade) {
        if (!memberId.equals(trade.getBorrowerMember().getId()) || trade.isTradeComplete() == false) {
            throw new ImpossibleWriteReviewException();
        }

        if (trade.isTradeComplete() == false) {
            throw new TradeNotCompleteException();
        }

        if (trade.isWrittenReview()) {
            throw new ExistReviewException();
        }
    }

}

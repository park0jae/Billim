package dblab.sharing_flatform.service.review;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.notification.NotificationType;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.notification.crud.create.NotificationRequestDto;
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
import dblab.sharing_flatform.helper.NotificationHelper;
import dblab.sharing_flatform.repository.emitter.EmitterRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.review.ReviewRepository;
import dblab.sharing_flatform.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;

    private final NotificationHelper notificationHelper;

    @Transactional
    public ReviewResponseDto writeReview(ReviewRequestDto reviewRequestDto, Long tradeId, String username){
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(TradeNotFoundException::new);
        Member reviewerMember = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new); // 리뷰 작성자
        Member member = memberRepository.findById(trade.getRenderMember().getId()).orElseThrow(MemberNotFoundException::new); // 리뷰 받는 사람

        validate(username, trade);

        Review review = new Review(reviewRequestDto.getContent(),
                reviewRequestDto.getStarRating(),
                reviewerMember,
                member);

        reviewRepository.save(review);
        trade.addReview(review);
        reviewerMember.calculateTotalStarRating(reviewRequestDto.getStarRating(), reviewRepository.countByMemberId(trade.getRenderMember().getId()));

        notificationHelper.notificationIfSubscribe(reviewerMember, member, NotificationType.REVIEW, "님이 거래에 대한 리뷰를 작성했습니다.");

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

    public List<ReviewResponseDto> findCurrentUserReviews(String username){
        return reviewRepository.findAllWithMemberByMemberId(username).stream().map(review -> ReviewResponseDto.toDto(review)).collect(Collectors.toList());
    }

    public Page<ReviewDto> findAllReviewsByUsername(ReviewPagingCondition cond){
        return reviewRepository.findAllByUsername(cond);
    }

    private void validate(String username, Trade trade) {
        if (username.equals(trade.getRenderMember().getUsername())) {
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

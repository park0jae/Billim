package dblab.sharing_flatform.service.review;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.review.ReviewDto;
import dblab.sharing_flatform.dto.review.crud.create.ReviewRequestDto;
import dblab.sharing_flatform.dto.review.crud.create.ReviewResponseDto;
import dblab.sharing_flatform.dto.review.crud.read.request.ReviewPagingCondition;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.review.ExistReviewException;
import dblab.sharing_flatform.exception.review.ImpossibleWriteReviewException;
import dblab.sharing_flatform.exception.review.ReviewNotFoundException;
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
    public ReviewResponseDto writeReview(ReviewRequestDto reviewRequestDto, Long id){
        Trade trade = tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new);

        if (trade.isTradeComplete() == false || !SecurityUtil.getCurrentUsername().get().equals(trade.getBorrowerMember().getUsername())) {
            throw new ImpossibleWriteReviewException();
        }
        if (trade.isWrittenReview()) {
            throw new ExistReviewException();
        }

        Review review = new Review(reviewRequestDto.getContent(),
                reviewRequestDto.getStarRating(),
                memberRepository.findByUsername(trade.getRenderMember().getUsername()).orElseThrow(MemberNotFoundException::new),
                memberRepository.findByUsername(trade.getBorrowerMember().getUsername()).orElseThrow(MemberNotFoundException::new)
                );

        reviewRepository.save(review);
        trade.addReview(review);

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
}

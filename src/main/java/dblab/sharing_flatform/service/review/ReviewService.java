package dblab.sharing_flatform.service.review;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.review.Review;
import dblab.sharing_flatform.domain.trade.Trade;
import dblab.sharing_flatform.dto.review.crud.create.ReviewRequestDto;
import dblab.sharing_flatform.dto.review.crud.create.ReviewResponseDto;
import dblab.sharing_flatform.dto.trade.crud.create.TradeResponseDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

        if(!trade.isTradeComplete()){
            throw new ImpossibleWriteReviewException();
        }
        if(reviewRepository.findByTradeId(id).isPresent()){
            throw new ExistReviewException();
        }

        Review review = new Review(reviewRequestDto.getContent(),
                reviewRequestDto.getStarRating(),
                memberRepository.findByUsername(trade.getRenderMember().getUsername()).orElseThrow(MemberNotFoundException::new),
                memberRepository.findByUsername(trade.getBorrowerMember().getUsername()).orElseThrow(MemberNotFoundException::new),
                trade
                );

        review.getMember().addReviews(review);
        reviewRepository.save(review);

        return ReviewResponseDto.toDto(review);
    }

    @Transactional
    public void deleteReview(Long id){
        reviewRepository.delete(reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new));
    }

    public List<ReviewResponseDto> findAllReviews(){
        return reviewRepository.findAll().stream().map(review -> ReviewResponseDto.toDto(review)).collect(Collectors.toList());
    }

    public List<ReviewResponseDto> findCurrentUserReviews(){
        String username = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        return reviewRepository.findAllByMember(username).stream().map(review -> ReviewResponseDto.toDto(review)).collect(Collectors.toList());
    }

    public List<ReviewResponseDto> findReviewsByAdmin(Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return reviewRepository.findAllByMember(member.getUsername()).stream().map(review -> ReviewResponseDto.toDto(review)).collect(Collectors.toList());
    }
}

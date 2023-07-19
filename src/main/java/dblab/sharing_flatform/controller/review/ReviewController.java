package dblab.sharing_flatform.controller.review;

import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.dto.review.crud.create.ReviewRequestDto;
import dblab.sharing_flatform.dto.review.crud.create.ReviewResponseDto;
import dblab.sharing_flatform.dto.review.crud.read.request.ReviewPagingCondition;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.review.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Review Controller", tags = "Review")
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "현재 로그인한 유저의 리뷰를 조회", notes = "현재 로그인한 유저에게 작성된 리뷰를 조회합니다.")
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Response findCurrentUserReviews(){
        Long memberId = Long.valueOf(SecurityUtil.getCurrentUserId().orElseThrow(AccessDeniedException::new));
        return Response.success(reviewService.findCurrentUserReviews(memberId));
    }
    @ApiOperation(value = "모든 리뷰 조회 (ADMIN 권한)", notes = "(ADMIN 권한으로) 작성된 모든 리뷰를 조회합니다.")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllReviews(){
        return Response.success(reviewService.findAllReviews());
    }

    @ApiOperation(value = "리뷰 검색 및 페이징", notes = "username으로 회원에 대한 리뷰를 조회 및 페이징합니다.")
    @GetMapping
    public Response findAllReviewByMemberId(ReviewPagingCondition cond){
        return Response.success(reviewService.findAllReviewsByUsername(cond));
    }

    @ApiOperation(value = "거래에 대한 리뷰 작성", notes = "거래 번호로 리뷰를 생성합니다.")
    @PostMapping("/{tradeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response writeReview(@PathVariable Long tradeId, @RequestBody ReviewRequestDto reviewRequestDto){
        String memberId = SecurityUtil.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        return Response.success(reviewService.writeReview(reviewRequestDto, tradeId, Long.valueOf(memberId)));
    }

    @ApiOperation(value = "거래에 대해 작성한 리뷰 삭제", notes = "현재 로그인한 유저에게 작성된 리뷰를 조회합니다.")
    @DeleteMapping("/{tradeId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteReview(@PathVariable Long tradeId){
        reviewService.deleteReview(tradeId);
        return Response.success();
    }

}

package dblab.sharing_platform.controller.review;

import dblab.sharing_platform.dto.response.Response;
import dblab.sharing_platform.dto.review.ReviewPagingCondition;
import dblab.sharing_platform.dto.review.ReviewRequestDto;
import dblab.sharing_platform.service.review.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static dblab.sharing_platform.config.security.util.SecurityUtil.getCurrentUsernameCheck;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Api(value = "Review Controller", tags = "Review")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "현재 로그인한 유저의 리뷰를 조회", notes = "현재 로그인한 유저에게 작성된 리뷰를 조회합니다.")
    @GetMapping("/my")
    @ResponseStatus(OK)
    public Response findAllReviewsWriteByCurrentUser(@Valid ReviewPagingCondition cond){
        cond.setUsername(getCurrentUsernameCheck());
        return Response.success(OK.value(), reviewService.findCurrentUserReviews(cond));
    }

    @ApiOperation(value = "모든 리뷰 조회 (ADMIN 권한)", notes = "(ADMIN 권한으로) 작성된 모든 리뷰를 조회합니다.")
    @GetMapping("/all")
    @ResponseStatus(OK)
    public Response findAllReviewWriteByAdmin(@Valid ReviewPagingCondition cond){
        return Response.success(OK.value(), reviewService.findAllReviewsWriteByAdmin(cond));
    }

    @ApiOperation(value = "특정 회원에 대한 리뷰를 페이징", notes = "특정 회원에 대한 리뷰를 페이징합니다.")
    @GetMapping
    @ResponseStatus(OK)
    public Response findAllReviewByNickname(@Valid ReviewPagingCondition cond){
        return Response.success(OK.value(), reviewService.findAllReviewsByNickname(cond));
    }

    @ApiOperation(value = "거래에 대한 리뷰 생성", notes = "거래에 대한 리뷰를 생성합니다.")
    @PostMapping("/{tradeId}")
    @ResponseStatus(CREATED)
    public Response writeReviewByTradeId(@ApiParam(value = "생성할 리뷰의 Trade Id", required = true) @PathVariable Long tradeId,
                                @Valid @RequestBody ReviewRequestDto reviewRequestDto){
        reviewService.writeReviewByTradeId(reviewRequestDto, tradeId, getCurrentUsernameCheck());
        return Response.success(CREATED.value());
    }

    @ApiOperation(value = "거래에 대해 작성한 리뷰 삭제", notes = "현재 로그인한 유저에게 작성된 리뷰를 조회합니다.")
    @DeleteMapping("/{tradeId}")
    @ResponseStatus(OK)
    public Response deleteReviewTradeId(@ApiParam(value = "삭제할 리뷰의 Trade Id", required = true) @PathVariable Long tradeId){
        reviewService.deleteReviewByTradeId(tradeId);
        return Response.success(OK.value());
    }
}

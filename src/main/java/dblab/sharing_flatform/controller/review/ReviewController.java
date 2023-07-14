package dblab.sharing_flatform.controller.review;

import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.dto.review.crud.create.ReviewRequestDto;
import dblab.sharing_flatform.dto.review.crud.create.ReviewResponseDto;
import dblab.sharing_flatform.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    // id에 해당하는 글에 리뷰를 작성
    @PostMapping("/{id}")
    public Response writeReview(@PathVariable Long id, @RequestBody ReviewRequestDto reviewRequestDto){
        return Response.success(reviewService.writeReview(reviewRequestDto, id));
    }

    // 전체 리뷰 조회
    @GetMapping
    public Response findAllReviews(){
        return Response.success(reviewService.findAllReviews());
    }

    // 현재 사용자 본인의 리뷰 조회
    @GetMapping("/user")
    public Response findCurrentUserReviews(){
        return Response.success(reviewService.findCurrentUserReviews());
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public Response deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return Response.success();
    }

    @GetMapping("/{id}")
    public Response findReviewsById(@PathVariable Long id){
        return Response.success(reviewService.findReviewsById(id));
    }
}

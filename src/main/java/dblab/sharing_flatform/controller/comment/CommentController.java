package dblab.sharing_flatform.controller.comment;

import dblab.sharing_flatform.dto.comment.CommentCreateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.comment.CommentService;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(value = "Comment Controller", tags = "Comment")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 특정 게시물의 댓글 전체 조회
    @ApiOperation(value = "게시글 번호로 댓글 전체 조회", notes = "게시글 ID로 게시글의 댓글을 전체 조회합니다.")
    @GetMapping("/post/{postId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@ApiParam(value = "Post ID", required = true) @PathVariable Long postId) {
        return Response.success(commentService.readAll(postId));
    }

    @ApiOperation(value = "댓글 생성", notes = "댓글을 생성합니다.")
    @PostMapping("/post/{postId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@ApiParam(value = "Post ID", required = true) @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto) {

        return Response.success(commentService.create(postId, requestDto, getCurrentUsername()));
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다.")
    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "삭제할 Comment ID", required = true) @PathVariable Long commentId) {
        commentService.delete(commentId);
        return Response.success();
    }

    private String getCurrentUsername() {
        return SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
    }


}

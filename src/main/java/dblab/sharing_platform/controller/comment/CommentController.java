package dblab.sharing_platform.controller.comment;

import dblab.sharing_platform.dto.comment.CommentCreateRequestDto;
import dblab.sharing_platform.dto.response.Response;
import dblab.sharing_platform.service.comment.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static dblab.sharing_platform.config.security.util.SecurityUtil.getCurrentUsernameCheck;


@Api(value = "Comment Controller", tags = "Comment")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 특정 게시물의 댓글 전체 조회
    @ApiOperation(value = "게시글 번호로 댓글 전체 조회", notes = "게시글 ID로 게시글의 댓글을 전체 조회합니다.")
    @GetMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Response readAllCommentByPostId(@ApiParam(value = "Post ID", required = true) @PathVariable Long postId) {
        return Response.success(commentService.readAllCommentByPostId(postId));
    }

    @ApiOperation(value = "댓글 생성", notes = "댓글을 생성합니다.")
    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createCommentWithPostId(@ApiParam(value = "Post ID", required = true) @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto) {
        return Response.success(commentService.createCommentWithPostId(postId, requestDto, getCurrentUsernameCheck()));
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다.")
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteCommentByCommentId(@ApiParam(value = "삭제할 Comment ID", required = true) @PathVariable Long commentId) {
        commentService.deleteCommentByCommentId(commentId);
        return Response.success();
    }
}

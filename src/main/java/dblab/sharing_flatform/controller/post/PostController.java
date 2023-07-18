package dblab.sharing_flatform.controller.post;

import dblab.sharing_flatform.dto.post.crud.create.PostCreateRequestDto;
import dblab.sharing_flatform.dto.post.crud.read.request.PostPagingCondition;
import dblab.sharing_flatform.dto.post.crud.update.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.post.PostService;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "Post Controller", tags = "Post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "게시글 검색 -> 목록 조회", notes = "검색조건에 따라 페이징하여 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid PostPagingCondition cond) {
        return Response.success(postService.readAll(cond));
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글 ID로 게시글을 조회합니다.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(postService.read(id));
    }

    @ApiOperation(value ="좋아요 누른 게시글 전체 조회", notes = "해당 번호의 게시글에 좋아요 남기기/좋아요 취소")
    @GetMapping("/like")
    @ResponseStatus(HttpStatus.OK)
    public Response readAllLikePost() {
        String id = SecurityUtil.getCurrentUserId().get();
        System.out.println("id = " + id);
        return Response.success(postService.readAllLikePost(Long.valueOf(id)));
    }

    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @ModelAttribute PostCreateRequestDto postCreateRequestDto) {
        postCreateRequestDto.setUsername(SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new));
        postService.create(postCreateRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "게시글 삭제", notes = "해당 번호의 게시글을 삭제한다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "삭제할 게시글 id", required = true) @PathVariable Long id) {
        postService.delete(id);
        return Response.success();
    }

    @ApiOperation(value = "게시글 수정", notes = "해당 번호의 게시글을 수정한다.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(@ApiParam(value = "수정할 게시글 id", required = true) @PathVariable Long id,
                           @Valid @ModelAttribute PostUpdateRequestDto postUpdateRequestDto) {
        return Response.success(postService.update(id, postUpdateRequestDto));
    }

    @ApiOperation(value = "게시글 좋아요/좋아요 취소", notes = "해당 번호의 게시글에 좋아요 남기기/좋아요 취소")
    @PostMapping("/like/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response likeUp(@ApiParam(value = "좋아요할 게시글 id", required = true) @PathVariable Long id) {
        String username = SecurityUtil.getCurrentUsername().orElseThrow(AccessDeniedException::new);
        postService.like(id, username);
        return Response.success();
    }

}

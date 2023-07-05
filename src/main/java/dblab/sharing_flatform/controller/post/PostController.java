package dblab.sharing_flatform.controller.post;

import dblab.sharing_flatform.dto.post.PostCreateRequestDto;
import dblab.sharing_flatform.dto.post.PostUpdateRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.auth.AccessDeniedException;
import dblab.sharing_flatform.service.post.PostService;
import dblab.sharing_flatform.util.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "게시글 생성", notes = "게시글을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
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

//    @ApiOperation(value = "게시글 수정", notes = "해당 번호의 게시글을 수정한다.")
//    @PatchMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public Response update(@ApiParam(value = "수정할 게시글 id", required = true) @PathVariable Long id,
//                           @Valid @ModelAttribute PostUpdateRequestDto postUpdateRequestDto) {
//        postService.update(id, postUpdateRequestDto);
//        return Response.success();
//    }
}

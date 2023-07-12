package dblab.sharing_flatform.dto.reply;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyCreateRequestDto {

    @ApiModelProperty(value = "댓글 내용", notes = "댓글 내용을 입력해주세요", required = true, example = "안녕하세요.")
    @NotBlank(message = "댓글 내용을 입력해주세요")
    private String content;

    @ApiModelProperty(hidden = true)
    @Null
    private String username;

    @ApiModelProperty(value = "게시글 ID", notes = "게시글 ID을 입력해주세요", required = true, example = "1")
    @NotNull(message = "게시글 ID를 입력해주세요.")
    private Long postId;

    @ApiModelProperty(value = "부모 댓글 아이디", notes = "부모 댓글 아이디를 입력해주세요", example = "7")
    private Long parentCommentId;
}

package dblab.sharing_flatform.dto.post;

import dblab.sharing_flatform.domain.item.Item;
import dblab.sharing_flatform.dto.item.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.item.ItemUpdateRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUpdateRequestDto {

    @ApiModelProperty(value = "게시글 제목", notes = "변경할 게시글 제목을 입력해주세요", required = true, example = "my title")
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "게시글 내용", notes = "변경할 게시글 내용 입력해주세요", required = true, example = "my content")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "카테고리 이름", notes = "변경할 카테고리 이름을 지정해주세요", required = true)
    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String categoryName;

    @ApiModelProperty(value = "물품", notes = "수정할 물품정보를 입력하세요.", required = false)
    private ItemUpdateRequestDto itemUpdateRequestDto;

    @ApiModelProperty(value = "추가 이미지", notes = "추가될 이미지 첨부")
    private List<MultipartFile> addImages = new ArrayList<>();

    @ApiModelProperty(value = "삭제할 이미지", notes = "삭제할 이미지 ID")
    private List<Long> deleteImageIds = new ArrayList<>();
}

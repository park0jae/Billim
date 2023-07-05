package dblab.sharing_flatform.dto.post;

import dblab.sharing_flatform.domain.item.Item;
import dblab.sharing_flatform.dto.item.ItemCreateRequestDto;
import dblab.sharing_flatform.dto.item.ItemUpdateRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
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
    private String title;

    @ApiModelProperty(value = "게시글 내용", notes = "변경할 게시글 내용 입력해주세요", required = true, example = "my content")
    private String content;

    @ApiModelProperty(value = "물품", notes = "수정할 물품정보를 입력하세요.", required = false)
    @Nullable
    private ItemUpdateRequestDto itemUpdateRequestDto;

    @ApiModelProperty(value = "추가 이미지", notes = "추가될 이미지 첨부")
    private List<MultipartFile> addImages = new ArrayList<>();

    @ApiModelProperty(value = "삭제할 이미지 이름", notes = "삭제할 이미지 이름")
    private List<String> deleteImageNames = new ArrayList<>();
}

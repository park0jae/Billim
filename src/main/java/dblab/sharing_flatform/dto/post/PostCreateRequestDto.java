package dblab.sharing_flatform.dto.post;

import dblab.sharing_flatform.domain.image.Image;
import dblab.sharing_flatform.domain.item.Item;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.ItemCreateRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel(value = "게시글 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateRequestDto {

    @ApiModelProperty(value = "게시글 제목", notes = "게시글 제목을 입력해주세요", required = true)
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "게시글 내용", notes = "게시글 내용 입력해주세요", required = true)
    @Lob
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "카테고리 이름", notes = "카테고리 이름을 지정해주세요", required = true)
    @NotBlank(message = "내용을 입력해주세요.")
    private String categoryName;

    @ApiModelProperty(value = "이미지", notes = "이미지를 첨부해주세요.")
    private List<MultipartFile> images = new ArrayList<>();

    @ApiModelProperty(value = "물품", notes = "물품을 첨부하세요.", required = false)
    private ItemCreateRequestDto itemCreateRequestDto;

    @ApiModelProperty(hidden = true)
    @Null
    private String username;

}

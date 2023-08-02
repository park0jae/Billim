package dblab.sharing_flatform.dto.image;

import dblab.sharing_flatform.domain.image.PostImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageDto {

    private Long id;
    private String uniqueName;
    private String originName;

    public static PostImageDto toDto(PostImage postImage) {
        return new PostImageDto(postImage.getId(), postImage.getUniqueName(), postImage.getOriginName());
    }

    public static List<PostImageDto> toDtoList(List<PostImage> postImages) {
        return postImages.stream().map(i -> PostImageDto.toDto(i)).collect(toList());
    }

}

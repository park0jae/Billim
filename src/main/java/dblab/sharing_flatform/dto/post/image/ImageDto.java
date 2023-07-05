package dblab.sharing_flatform.dto.post.image;

import dblab.sharing_flatform.domain.image.Image;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDto {

    private Long id;
    private String uniqueName;
    private String originName;

    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getUniqueName(), image.getOriginName());
    }

    public static List<ImageDto> toDtoList(List<Image> images) {
        return images.stream().map(i -> ImageDto.toDto(i)).collect(toList());
    }

}

package dblab.sharing_flatform.dto.post.crud.read;

import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.ItemDto;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.image.ImageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReadResponseDto {

    private Long id;
    private String title;
    private String content;
    private MemberDto memberResponseDto;
    private ItemDto itemDto;
    private List<ImageDto> imageDtoList;

    public static PostReadResponseDto toDto(Post post) {
        return new PostReadResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                MemberDto.toDto(post.getMember()),
                ItemDto.toDto(post.getItem()),
                post.getImages().stream().map(i -> ImageDto.toDto(i)).collect(Collectors.toList()));
    }
}

package dblab.sharing_flatform.dto.post.crud.read.response;

import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.ItemDto;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.image.postImage.PostImageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReadResponseDto {

    private Long id;
    private String title;
    private String content;
    private MemberDto writer;

    @Nullable
    private ItemDto item;
    private List<PostImageDto> imageList;

    public static PostReadResponseDto toDto(Post post) {
        return new PostReadResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                MemberDto.toDto(post.getMember()),
                Optional.ofNullable(ItemDto.toDto(post.getItem())).orElse(null),
                post.getPostImages().stream().map(i -> PostImageDto.toDto(i)).collect(Collectors.toList()));
    }
}

package dblab.sharing_flatform.dto.post;

import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.dto.item.ItemDto;
import dblab.sharing_flatform.dto.member.MemberDto;
import dblab.sharing_flatform.dto.image.PostImageDto;
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
    private String categoryName;
    private String title;
    private String content;
    private Integer likes;
    private MemberDto writer;
    private ItemDto item;
    private List<PostImageDto> imageList;

    public static PostReadResponseDto toDto(Post post) {
        return new PostReadResponseDto(
                post.getId(),
                post.getCategory().getName(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                MemberDto.toDto(post.getMember()),
                ItemDto.toDto(post.getItem()),
                post.getPostImages().stream().map(i -> PostImageDto.toDto(i)).collect(Collectors.toList()));
    }
}

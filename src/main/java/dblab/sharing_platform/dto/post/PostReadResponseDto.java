package dblab.sharing_platform.dto.post;

import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.dto.item.ItemDto;
import dblab.sharing_platform.dto.member.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<String> links;

    public static PostReadResponseDto toDto(Post post, List<String> links) {
        return new PostReadResponseDto(
                post.getId(),
                post.getCategory().getName(),
                post.getTitle(),
                post.getContent(),
                post.getLikes(),
                MemberDto.toDto(post.getMember()),
                ItemDto.toDto(post.getItem()),
                links);
    }
}

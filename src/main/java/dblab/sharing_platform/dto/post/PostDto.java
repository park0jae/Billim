package dblab.sharing_platform.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import dblab.sharing_platform.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

    private Long id;
    private String title;
    private String nickname;
    private String link;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;


    public static PostDto toDto(Post post, String imageLink) {
        return new PostDto(post.getId(),
                post.getTitle(),
                post.getMember().getNickname(),
                imageLink,
                post.getCreatedTime());
    }

    public static List<PostDto> toDtoList(List<Post> posts, List<String> imageLinks) {
        List<PostDto> postDtoList = IntStream.range(0, posts.size())
                .mapToObj(k -> PostDto.toDto(posts.get(k), imageLinks.get(k)))
                .collect(Collectors.toList());

        return postDtoList;
    }
}

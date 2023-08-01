package dblab.sharing_flatform.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import dblab.sharing_flatform.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

    private Long id;
    private String title;
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    public static PostDto toDto(Post post) {
        return new PostDto(post.getId(),
                post.getTitle(),
                post.getMember().getNickname(),
                post.getCreatedTime());
    }

    public static List<PostDto> toDtoList(List<Post> posts) {
        return posts.stream().map(i -> PostDto.toDto(i)).collect(Collectors.toList());
    }
}

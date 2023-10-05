package dblab.sharing_platform.dto.post;

import dblab.sharing_platform.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateResponseDto {

    private Long id;
    public static PostCreateResponseDto toDto(Post post) {
        return new PostCreateResponseDto(post.getId());
    }
}

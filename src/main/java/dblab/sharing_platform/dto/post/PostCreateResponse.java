package dblab.sharing_platform.dto.post;

import dblab.sharing_platform.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateResponse {

    private Long id;
    public static PostCreateResponse toDto(Post post) {
        return new PostCreateResponse(post.getId());
    }
}

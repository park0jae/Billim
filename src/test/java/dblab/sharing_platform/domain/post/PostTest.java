package dblab.sharing_platform.domain.post;

import dblab.sharing_platform.domain.image.PostImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dblab.sharing_platform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    @DisplayName("게시글 저장")
    public void createPostTest() throws Exception {
        //given
        Post post = createPost();

        //when
        Integer likes = post.getLikes();

        //then
        assertThat(likes).isEqualTo(0);
    }

    @Test
    @DisplayName("PostImage -> Post 다대일 매핑")
    public void addImagesTest() throws Exception {
        //given
        Post post = createPost();

        //when
        List<PostImage> postImages = post.getPostImages();

        //then
        postImages.stream().forEach(i -> assertThat(i.getPost()).isEqualTo(post));
    }

}

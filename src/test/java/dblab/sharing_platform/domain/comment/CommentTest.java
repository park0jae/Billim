package dblab.sharing_platform.domain.comment;

import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.factory.comment.CommentFactory;
import dblab.sharing_platform.factory.member.MemberFactory;
import dblab.sharing_platform.factory.post.PostFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentTest {

    @Test
    @DisplayName("댓글 생성")
    public void createComment() throws Exception {
        //given
        Post post = PostFactory.createPost();
        Member member = MemberFactory.createMember();
        Comment comment = CommentFactory.createComment(post, member, null);

        //then
        assertThat(comment).isNotNull();
    }

    @Test
    @DisplayName("댓글 삭제")
    public void deleteTest() throws Exception {
        //given
        Post post = PostFactory.createPost();
        Member member = MemberFactory.createMember();
        Comment comment = CommentFactory.createComment(post, member, null);

        //when
        comment.delete();

        //then
        assertThat(comment.getContent()).isEqualTo("(삭제된 댓글입니다.)");
        assertThat(comment.getMember()).isNull();
        assertThat(comment.isDeleted()).isTrue();
    }

}
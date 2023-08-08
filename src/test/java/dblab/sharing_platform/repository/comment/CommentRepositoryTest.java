package dblab.sharing_platform.repository.comment;

import dblab.sharing_platform.config.querydsl.QuerydslConfig;
import dblab.sharing_platform.domain.category.Category;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.domain.comment.Comment;
import dblab.sharing_platform.factory.post.PostFactory;
import dblab.sharing_platform.repository.category.CategoryRepository;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static dblab.sharing_platform.factory.comment.CommentFactory.createComment;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
class CommentRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommentRepository commentRepository;

    Post post;
    Category category;
    Member member;

    public void clear() {
        em.flush();
        em.clear();
    }
    @BeforeEach
    public void beforeEach() {
        post = PostFactory.createPost();
        member = post.getMember();
        category = post.getCategory();

        memberRepository.save(member);
        clear();

        categoryRepository.save(category);
        clear();

        postRepository.save(post);
        clear();
    }

    @Test
    @DisplayName("Post ID로 게시글 댓글 계층형 조회")
    public void findAllByPostIdTest() throws Exception {
        // given
        // 1		NULL
        // 2		1
        // 3		NULL
        // 4		NULL
        // 5		1
        // 6		NULL
        // 7		6
        // 8		NULL
        Comment comment1 = commentRepository.save(createComment(post, member, null));
        Comment comment2 = commentRepository.save(createComment(post, member, comment1));
        Comment comment3 = commentRepository.save(createComment(post, member, null));
        Comment comment4 = commentRepository.save(createComment(post, member, null));
        Comment comment5 = commentRepository.save(createComment(post, member, comment1));
        Comment comment6 = commentRepository.save(createComment(post, member, null));
        Comment comment7 = commentRepository.save(createComment(post, member, comment6));
        Comment comment8 = commentRepository.save(createComment(post, member, null));
        clear();

        //when
        // 1		NULL
        // 3		NULL
        // 4		NULL
        // 6		NULL
        // 8		NULL
        // 2		1
        // 5		1
        // 7		6
        List<Comment> result = commentRepository.findAllOrderByParentIdAscNullsFirstByPostId(post.getId());

        //then
        assertThat(result.get(0).getId()).isEqualTo(comment1.getId());
        assertThat(result.get(1).getId()).isEqualTo(comment3.getId());
        assertThat(result.get(2).getId()).isEqualTo(comment4.getId());
        assertThat(result.get(3).getId()).isEqualTo(comment6.getId());
        assertThat(result.get(4).getId()).isEqualTo(comment8.getId());
        assertThat(result.get(5).getId()).isEqualTo(comment2.getId());
        assertThat(result.get(6).getId()).isEqualTo(comment5.getId());
        assertThat(result.get(7).getId()).isEqualTo(comment7.getId());
        assertThat(result.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("회원 삭제 -> 작성한 댓글 삭제 By cascade")
    public void deleteCascadeOnMemberTest() throws Exception {
        //given
        commentRepository.save(createComment(post, member, null));
        clear();

        //when
        memberRepository.delete(member);
        clear();

        //then
        assertThat(commentRepository.count()).isZero();
    }

    @Test
    @DisplayName("게시글 삭제 -> 작성한 댓글 삭제 By cascade")
    public void deleteCascadeOnPostTest() throws Exception {
        //given
        commentRepository.save(createComment(post, member, null));
        clear();

        //when
        postRepository.delete(post);
        clear();

        //then
        assertThat(commentRepository.count()).isZero();
    }

    @Test
    @DisplayName("부모 댓글 삭제 -> 하위 댓글 삭제 By cascade")
    public void deleteCascadeOnParentCommentTest() throws Exception {
        //given
        Comment parent = commentRepository.save(createComment(post, member, null));
        commentRepository.save(createComment(post, member, parent));
        clear();

        //when
        commentRepository.delete(parent);
        clear();

        //then
        assertThat(commentRepository.count()).isZero();
    }
}
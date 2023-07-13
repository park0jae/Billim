package dblab.sharing_flatform.repository.comment;

import dblab.sharing_flatform.config.querydsl.QuerydslConfig;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.comment.Comment;
import dblab.sharing_flatform.factory.post.PostFactory;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static dblab.sharing_flatform.factory.comment.CommentFactory.createReply;
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

    @Transactional
    public void init() {
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
        init();
        Comment comment1 = commentRepository.save(createReply(post, member, null));
        Comment comment2 = commentRepository.save(createReply(post, member, comment1));
        Comment comment3 = commentRepository.save(createReply(post, member, null));
        Comment comment4 = commentRepository.save(createReply(post, member, null));
        Comment comment5 = commentRepository.save(createReply(post, member, comment1));
        Comment comment6 = commentRepository.save(createReply(post, member, null));
        Comment comment7 = commentRepository.save(createReply(post, member, comment6));
        Comment comment8 = commentRepository.save(createReply(post, member, null));
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

}
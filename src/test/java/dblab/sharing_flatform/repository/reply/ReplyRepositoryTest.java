package dblab.sharing_flatform.repository.reply;

import dblab.sharing_flatform.config.querydsl.QuerydslConfig;
import dblab.sharing_flatform.domain.category.Category;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.post.Post;
import dblab.sharing_flatform.domain.reply.Reply;
import dblab.sharing_flatform.factory.post.PostFactory;
import dblab.sharing_flatform.repository.category.CategoryRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static dblab.sharing_flatform.factory.reply.ReplyFactory.createReply;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class ReplyRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReplyRepository replyRepository;

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
        Reply reply1 = replyRepository.save(createReply(post, member, null));
        Reply reply2 = replyRepository.save(createReply(post, member, reply1));
        Reply reply3 = replyRepository.save(createReply(post, member, null));
        Reply reply4 = replyRepository.save(createReply(post, member, null));
        Reply reply5 = replyRepository.save(createReply(post, member, reply1));
        Reply reply6 = replyRepository.save(createReply(post, member, null));
        Reply reply7 = replyRepository.save(createReply(post, member, reply6));
        Reply reply8 = replyRepository.save(createReply(post, member, null));
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
        List<Reply> result = replyRepository.findAllOrderByParentIdAscNullsFirstByPostId(post.getId());

        //then
        assertThat(result.get(0).getId()).isEqualTo(reply1.getId());
        assertThat(result.get(1).getId()).isEqualTo(reply3.getId());
        assertThat(result.get(2).getId()).isEqualTo(reply4.getId());
        assertThat(result.get(3).getId()).isEqualTo(reply6.getId());
        assertThat(result.get(4).getId()).isEqualTo(reply8.getId());
        assertThat(result.get(5).getId()).isEqualTo(reply2.getId());
        assertThat(result.get(6).getId()).isEqualTo(reply5.getId());
        assertThat(result.get(7).getId()).isEqualTo(reply7.getId());
        assertThat(result.size()).isEqualTo(8);
    }

}
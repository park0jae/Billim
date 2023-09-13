package dblab.sharing_platform.repository.message;

import dblab.sharing_platform.config.querydsl.QuerydslConfig;
import dblab.sharing_platform.domain.category.Category;
import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.message.Message;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.dto.message.MessageDto;
import dblab.sharing_platform.dto.message.MessagePagingCondition;
import dblab.sharing_platform.factory.member.MemberFactory;
import dblab.sharing_platform.repository.category.CategoryRepository;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static dblab.sharing_platform.factory.post.PostFactory.createPost;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
public class MessageRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Message message;

    Post post;
    Member sendMember;
    Member receiveMember;
    Category category;

    @BeforeEach
    public void beforeEach(){
        post = createPost();
        receiveMember = MemberFactory.createReceiveMember();
        sendMember = post.getMember();
        category = post.getCategory();

        memberRepository.save(sendMember);
        flushAndClear();

        categoryRepository.save(category);
        flushAndClear();

        postRepository.save(post);
        flushAndClear();

        memberRepository.save(receiveMember);
        flushAndClear();

        message = new Message("content", receiveMember, sendMember, post);
        System.out.println("post.getId() = " + post.getId());

    }

    @Test
    @DisplayName("송신 메시지 조회")
    public void findAllBySendMemberTest() {
        // given
        String senderName = message.getSendMember().getNickname();
        messageRepository.save(message);
        flushAndClear();

        // when
        MessagePagingCondition cond = new MessagePagingCondition(0, 10, sendMember.getUsername(), null, null, null);
        Page<MessageDto> result = messageRepository.findAllBySendMember(cond);

        // then
        assertThat(senderName).isEqualTo(result.getContent().get(0).getSenderNickname());
    }

    @Test
    @DisplayName("수신 메시지 조회")
    public void findAllByReceiverMemberTest(){
        // given
        String receiverName = message.getReceiveMember().getNickname();
        messageRepository.save(message);

        flushAndClear();

        // when
        MessagePagingCondition cond = new MessagePagingCondition(0, 10, null, receiveMember.getUsername(), null, null);
        Page<MessageDto> result = messageRepository.findAllByReceiverMember(cond);

        // then
        assertThat(receiverName).isEqualTo(result.getContent().get(0).getReceiverNickname());
    }


    public void flushAndClear(){
        em.flush();
        em.clear();
    }

}

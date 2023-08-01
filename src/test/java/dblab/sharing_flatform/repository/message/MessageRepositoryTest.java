package dblab.sharing_flatform.repository.message;

import dblab.sharing_flatform.config.querydsl.QuerydslConfig;
import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.factory.member.MemberFactory;
import dblab.sharing_flatform.factory.message.MessageFactory;
import dblab.sharing_flatform.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static dblab.sharing_flatform.factory.message.MessageFactory.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
public class MessageRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MemberRepository memberRepository;

    Message message;

    @BeforeEach
    public void beforeEach(){
        Member receiveMember = MemberFactory.createReceiveMember();
        Member sendMember = MemberFactory.createSendMember();

        memberRepository.save(receiveMember);
        flushAndClear();
        memberRepository.save(sendMember);
        flushAndClear();

        message = new Message("content", receiveMember, sendMember);
    }

    @Test
    @DisplayName("송신 메시지 조회")
    public void findAllBySendMemberTest() {
        // given
        String senderName = message.getSendMember().getUsername();

        messageRepository.save(message);
        flushAndClear();

        // when
        List<Message> allSendMessages = messageRepository.findAllBySendMember(senderName);

        // then
        assertThat(senderName).isEqualTo(allSendMessages.get(0).getSendMember().getUsername());
    }

    @Test
    @DisplayName("수신 메시지 조회")
    public void findAllByReceiverMemberTest(){
        // given
        String receiverName = message.getReceiveMember().getUsername();
        messageRepository.save(message);
        flushAndClear();

        // when
        List<Message> allReceiveMessages = messageRepository.findAllByReceiverMember(receiverName);

        // then
        assertThat(receiverName).isEqualTo(allReceiveMessages.get(0).getReceiveMember().getUsername());
    }

    @Test
    @DisplayName("특정 회원이 특정 회원에게 보낸 메시지 전체 조회")
    public void findAllBySendAndReceiverMembersTest(){
        // given
        messageRepository.save(message);
        flushAndClear();

        // when
        List<Message> result = messageRepository.findAllBySendAndReceiverMembers(
                message.getSendMember().getNickname(),
                message.getReceiveMember().getNickname());
        // then
        assertThat(result.get(0).getSendMember().getUsername()).isEqualTo("sender");
        assertThat(result.get(0).getReceiveMember().getUsername()).isEqualTo("receiver");
    }

    public void flushAndClear(){
        em.flush();
        em.clear();
    }

}

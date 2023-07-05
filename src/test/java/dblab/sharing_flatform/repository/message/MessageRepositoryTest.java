package dblab.sharing_flatform.repository.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.factory.member.MemberFactory;
import dblab.sharing_flatform.factory.message.MessageFactory;
import dblab.sharing_flatform.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static dblab.sharing_flatform.factory.message.MessageFactory.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
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
    public void findAllBySendAndReceiverMembersTest(){
        // given
        messageRepository.save(message);
        flushAndClear();

        // when
        List<Message> allBySendAndReceiverMembers = messageRepository.findAllBySendAndReceiverMembers(
                message.getSendMember().getUsername(),
                message.getReceiveMember().getUsername());
        // then
        assertThat(allBySendAndReceiverMembers.get(0).getSendMember().getUsername()).isEqualTo("sender");
        assertThat(allBySendAndReceiverMembers.get(0).getReceiveMember().getUsername()).isEqualTo("receiver");
    }

    public void flushAndClear(){
        em.flush();
        em.clear();
    }

}

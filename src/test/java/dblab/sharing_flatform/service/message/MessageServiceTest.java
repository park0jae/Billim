package dblab.sharing_flatform.service.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.dto.message.MessageRequestDto;
import dblab.sharing_flatform.dto.message.MessageResponseDto;
import dblab.sharing_flatform.factory.member.MemberFactory;
import dblab.sharing_flatform.factory.message.MessageFactory;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.message.MessageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MemberRepository memberRepository;

    Member receiveMember;
    Member sendMember;

    @BeforeEach
    public void beforeEach(){
            receiveMember = MemberFactory.createReceiveMember();
            sendMember = MemberFactory.createSendMember();
    }

    @Test
    public void sendMessageTest() {
        // Given
        MessageRequestDto messageRequestDto = new MessageRequestDto("HelloWorld","receiver","sender");

        given(memberRepository.findByUsername("sender")).willReturn(Optional.of(sendMember));
        given(memberRepository.findByUsername("receiver")).willReturn(Optional.of(receiveMember));

        // When
        MessageResponseDto result = messageService.sendMessage(messageRequestDto);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    public void findSendMessageTest() {
        // Given

        Message message = new Message("content", receiveMember, sendMember);

        // When
        List<MessageResponseDto> sendMessages = messageService.findSendMessage(message.getSendMember().getUsername());

        // Then
        assertThat(sendMessages).isNotNull();
    }

    @Test
    public void findReceiveMessageTest(){
        // Given

        Message message = new Message("content", receiveMember, sendMember);

        // When
        List<MessageResponseDto> receiveMessages = messageService.findReceiveMessage(message.getSendMember().getUsername());

        // Then
        assertThat(receiveMessages).isNotNull();
    }


    @Test
    public void findSendMessageToMemberTest(){
        // Given
        given(memberRepository.findById(receiveMember.getId())).willReturn(Optional.of(receiveMember));

        // When
        List<MessageResponseDto> sendMessageToMembers = messageService.findSendMessageToMember(sendMember.getUsername(), receiveMember.getId());

        // Then
        assertThat(sendMessageToMembers).isNotNull();
    }

    @Test
    public void findReceiveMessageByMember(){
        // Given
        Message message = new Message("content", receiveMember, sendMember);

        given(memberRepository.findById(sendMember.getId())).willReturn(Optional.of(sendMember));

        // When
        List<MessageResponseDto> receiveMessageByMembers = messageService.findReceiveMessageByMember(receiveMember.getUsername(), receiveMember.getId());

        // Then
        assertThat(receiveMessageByMembers).isNotNull();
    }

    @Test
    public void deleteMessageBySender(){
        //Given
        Message message = new Message("content", receiveMember, sendMember);

        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

        // When
        messageService.deleteMessageBySender(receiveMember.getId());

        // Then
        assertThat(message.isDeleteBySender()).isTrue();
    }

    @Test
    public void deleteMessageByReceiver(){
        //Given
        Message message = new Message("content", receiveMember, sendMember);

        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

        // When
        messageService.deleteMessageByReceiver(sendMember.getId());

        // Then
        assertThat(message.isDeleteByReceiver()).isTrue();
    }

}

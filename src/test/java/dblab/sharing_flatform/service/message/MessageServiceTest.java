package dblab.sharing_flatform.service.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.dto.message.crud.create.MessageCreateRequestDto;
import dblab.sharing_flatform.dto.message.MessageDto;
import dblab.sharing_flatform.exception.message.MessageNotFoundException;
import dblab.sharing_flatform.factory.member.MemberFactory;
import dblab.sharing_flatform.factory.message.MessageFactory;
import dblab.sharing_flatform.helper.NotificationHelper;
import dblab.sharing_flatform.repository.emitter.EmitterRepository;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.message.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dblab.sharing_flatform.factory.member.MemberFactory.createReceiveMember;
import static dblab.sharing_flatform.factory.member.MemberFactory.createSendMember;
import static dblab.sharing_flatform.factory.message.MessageFactory.*;
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

    @Mock
    private NotificationHelper helper;

    Member receiveMember;
    Member sendMember;
    Message message;

    @BeforeEach
    public void beforeEach(){
        receiveMember = createReceiveMember();
        sendMember = createSendMember();
        message = createMessageWithMeber(receiveMember, sendMember);
    }

    @Test
    @DisplayName("메세지 생성 및 전송 테스트")
    public void sendMessageTest() {
        // Given
        MessageCreateRequestDto messageCreateRequestDto = new MessageCreateRequestDto("HelloWorld", "receiver");

        given(memberRepository.findByUsername(sendMember.getUsername())).willReturn(Optional.of(sendMember));
        given(memberRepository.findByNickname("receiver")).willReturn(Optional.of(receiveMember));

        // When
        MessageDto result = messageService.sendMessage(messageCreateRequestDto, sendMember.getUsername());

        // Then
        assertThat(result.getContent()).isEqualTo("HelloWorld");
    }

    @Test
    @DisplayName("송신 메세지 조회 테스트")
    public void findSendMessageTest() {
        // Given
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        given(messageRepository.findAllBySendMember(sendMember.getUsername())).willReturn(messages);

        // When
        List<MessageDto> result = messageService.findSendMessage(message.getSendMember().getUsername());

        // Then
        assertThat(result.get(0).getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("수신 메세지 조회 테스트")
    public void findReceiveMessageTest(){
        // Given
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        given(messageRepository.findAllByReceiverMember(receiveMember.getUsername())).willReturn(messages);

        // When
        List<MessageDto> result = messageService.findReceiveMessage(message.getReceiveMember().getUsername());

        // Then
        assertThat(result.get(0).getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("멤버1 -> 멤버2 전송 메세지 조회 테스트")
    public void findMessageMemberToMemberTest(){
        // Given
        List<Message> messages = new ArrayList<>();
        messages.add(createMessageWithMeber(receiveMember, sendMember));
        messages.add(createMessageWithMeber(receiveMember, sendMember));

        given(memberRepository.findByNickname(receiveMember.getNickname())).willReturn(Optional.of(receiveMember));
        given(messageRepository.findAllBySendAndReceiverMembers(sendMember.getUsername(), receiveMember.getNickname())).willReturn(messages);

        // When
        List<MessageDto> result = messageService.findSendMessageToMember(sendMember.getUsername(), receiveMember.getNickname());

        // Then
        assertThat(result).hasSize(2);

        MessageDto messageDto1 = result.get(0);
        assertThat(messageDto1.getContent()).isEqualTo("content");

        MessageDto messageDto2 = result.get(1);
        assertThat(messageDto2.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("송신자에 의한 메세지 삭제 테스트")
    public void deleteMessageBySender(){
        //Given
        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

        // When
        messageService.deleteMessageBySender(receiveMember.getId());

        // Then
        assertThat(message.isDeleteBySender()).isTrue();
    }

    @Test
    @DisplayName("수신자에 의한 메세지 삭제 테스트")
    public void deleteMessageByReceiver(){
        //Given
        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

        // When
        messageService.deleteMessageByReceiver(sendMember.getId());

        // Then
        assertThat(message.isDeleteByReceiver()).isTrue();
    }

    @Test
    @DisplayName("메세지 존재 X , 예외 테스트")
    public void messageNotFoundExceptionTest(){
        // Given
        Long messageId = 100L;
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> messageService.deleteMessageBySender(messageId)).isInstanceOf(MessageNotFoundException.class);
    }

}

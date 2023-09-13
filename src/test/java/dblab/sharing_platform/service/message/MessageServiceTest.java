package dblab.sharing_platform.service.message;

import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.message.Message;
import dblab.sharing_platform.domain.post.Post;
import dblab.sharing_platform.dto.message.MessageDto;
import dblab.sharing_platform.dto.message.MessagePagingCondition;
import dblab.sharing_platform.dto.message.PagedMessageListDto;
import dblab.sharing_platform.exception.message.MessageNotFoundException;
import dblab.sharing_platform.helper.NotificationHelper;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.message.MessageRepository;
import dblab.sharing_platform.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dblab.sharing_platform.factory.member.MemberFactory.createReceiveMember;
import static dblab.sharing_platform.factory.member.MemberFactory.createSendMember;
import static dblab.sharing_platform.factory.message.MessageFactory.createMessageWithMeber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private PostRepository postRepository;

    @Mock
    private NotificationHelper helper;

    Member receiveMember;
    Member sendMember;
    Message message;
    Post post;

    @BeforeEach
    public void beforeEach(){
        receiveMember = createReceiveMember();
        sendMember = createSendMember();
        message = createMessageWithMeber(receiveMember, sendMember);
        post = message.getPost();
    }

//    @Test
//    @DisplayName("메세지 생성 및 전송 테스트")
//    public void sendMessageTest() {
//        // Given
//        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
//        given(memberRepository.findByUsername(sendMember.getUsername())).willReturn(Optional.of(sendMember));
//        given(memberRepository.findByNickname(receiveMember.getNickname())).willReturn(Optional.of(receiveMember));
//
//        MessageCreateRequestDto messageCreateRequestDto = new MessageCreateRequestDto("HelloWorld", receiveMember.getNickname(), post.getId());
//        // When
//        MessageDto result = messageService.sendMessageToReceiverByCurrentUser(messageCreateRequestDto, sendMember.getUsername());
//
//        // Then
//        assertThat(result.getContent()).isEqualTo("HelloWorld");
//    }

    @Test
    @DisplayName("송신 메세지 조회 테스트")
    public void findSendMessageTest() {
        // Given
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        MessagePagingCondition cond = new MessagePagingCondition(0, 10, sendMember.getUsername(), null, null, null);
        given(messageRepository.findAllBySendMember(cond)).willReturn(
                new PageImpl<>(List.of(MessageDto.toDto(messages.get(0))), PageRequest.of(cond.getPage(), cond.getSize()), 1));

        // When
        PagedMessageListDto result = messageService.findSendMessageByCurrentUser(cond);

        // Then
        assertThat(result.getMessageList().get(0).getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("수신 메세지 조회 테스트")
    public void findReceiveMessageTest(){
        // Given
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        MessagePagingCondition cond = new MessagePagingCondition(0, 10, null, receiveMember.getUsername(), null, null);
        given(messageRepository.findAllByReceiverMember(cond)).willReturn(
                new PageImpl<>(List.of(MessageDto.toDto(messages.get(0))), PageRequest.of(cond.getPage(), cond.getSize()), 1));

        // When
        PagedMessageListDto result = messageService.findReceiveMessageByCurrentUser(cond);

        // Then
        assertThat(result.getMessageList().get(0).getContent()).isEqualTo("content");
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

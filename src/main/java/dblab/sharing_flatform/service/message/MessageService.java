package dblab.sharing_flatform.service.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.domain.notification.NotificationType;
import dblab.sharing_flatform.dto.message.crud.create.MessageCreateRequestDto;
import dblab.sharing_flatform.dto.message.MessageDto;
import dblab.sharing_flatform.dto.notification.crud.create.NotificationRequestDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.message.MessageNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.message.MessageRepository;
import dblab.sharing_flatform.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public MessageDto sendMessage(MessageCreateRequestDto requestDto) {
        Message message = new Message(requestDto.getContent(),
                memberRepository.findByUsername(requestDto.getReceiveMember()).orElseThrow(MemberNotFoundException::new),
                memberRepository.findByUsername(requestDto.getSendMember()).orElseThrow(MemberNotFoundException::new));

        messageRepository.save(message);

        NotificationRequestDto notificationRequestDto = new NotificationRequestDto("새로운 메시지 도착", String.valueOf(NotificationType.MESSAGE), requestDto.getReceiveMember());
        eventPublisher.publishEvent(notificationRequestDto);

        return MessageDto.toDto(message);
    }

    public List<MessageDto> findSendMessage(String senderName){
        List<Message> messages = messageRepository.findAllBySendMember(senderName);

        return messages.stream().map(message-> MessageDto.toDto(message)).collect(Collectors.toList());
    }

    public List<MessageDto> findReceiveMessage(String ReceiverName){
        List<Message> messages = messageRepository.findAllByReceiverMember(ReceiverName);

        return messages.stream().map(message -> MessageDto.toDto(message)).collect(Collectors.toList());

    }

    public List<MessageDto> findMessageMemberToMember(String username, String toUsername){
        memberRepository.findByUsername(toUsername).orElseThrow(MemberNotFoundException::new);
        List<Message> messages = messageRepository.findAllBySendAndReceiverMembers(username, toUsername);
        return messages.stream().map(message -> MessageDto.toDto(message)).collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessageBySender(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        message.deleteBySender();

        if (message.isDeleteBySender() && message.isDeleteByReceiver()) {
            messageRepository.delete(message);
        }
    }

    @Transactional
    public void deleteMessageByReceiver(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        message.deleteByReceiver();

        if (message.isDeleteBySender() && message.isDeleteByReceiver()) {
            messageRepository.delete(message);
        }
    }


}
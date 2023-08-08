package dblab.sharing_platform.service.message;

import dblab.sharing_platform.domain.member.Member;
import dblab.sharing_platform.domain.message.Message;
import dblab.sharing_platform.domain.notification.NotificationType;
import dblab.sharing_platform.dto.message.MessageCreateRequestDto;
import dblab.sharing_platform.dto.message.MessageDto;
import dblab.sharing_platform.dto.message.MessagePagingCondition;
import dblab.sharing_platform.dto.message.PagedMessageListDto;
import dblab.sharing_platform.exception.member.MemberNotFoundException;
import dblab.sharing_platform.exception.message.MessageNotFoundException;
import dblab.sharing_platform.helper.NotificationHelper;
import dblab.sharing_platform.repository.member.MemberRepository;
import dblab.sharing_platform.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final NotificationHelper notificationHelper;
    private static final String ARRIVE_MESSAGE = "님으로부터 메시지가 도착했습니다.";

    @Transactional
    public MessageDto sendMessageToReceiverByCurrentUser(MessageCreateRequestDto requestDto, String username) {
        Member sender = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByNickname(requestDto.getReceiveMember()).orElseThrow(MemberNotFoundException::new);
        Message message = new Message(requestDto.getContent(), receiver, sender);

        messageRepository.save(message);
        notificationHelper.notificationIfSubscribe(sender, receiver, NotificationType.MESSAGE, ARRIVE_MESSAGE);

        return MessageDto.toDto(message);
    }

    public PagedMessageListDto findSendMessageByCurrentUser(MessagePagingCondition cond){
        return PagedMessageListDto.toDto(messageRepository.findAllBySendMember(cond));
    }

    public PagedMessageListDto findReceiveMessageByCurrentUser(MessagePagingCondition cond){
        return PagedMessageListDto.toDto(messageRepository.findAllByReceiverMember(cond));
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
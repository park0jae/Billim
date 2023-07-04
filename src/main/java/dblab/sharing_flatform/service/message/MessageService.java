package dblab.sharing_flatform.service.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.dto.message.MessageRequestDto;
import dblab.sharing_flatform.dto.message.MessageResponseDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.message.MessageNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.message.MessageRepository;
import dblab.sharing_flatform.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public MessageResponseDto sendMessage(MessageRequestDto messageRequestDto) {

        Message message = new Message(messageRequestDto.getContent(),
                memberRepository.findByUsername(messageRequestDto.getReceiveMember()).orElseThrow(MemberNotFoundException::new),
                memberRepository.findByUsername(messageRequestDto.getSendMember()).orElseThrow(MemberNotFoundException::new));

        messageRepository.save(message);
        return MessageResponseDto.toDto(message);
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
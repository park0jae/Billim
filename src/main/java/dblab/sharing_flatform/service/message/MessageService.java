package dblab.sharing_flatform.service.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import dblab.sharing_flatform.dto.message.MessageRequestDto;
import dblab.sharing_flatform.dto.message.MessageResponseDto;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.message.MessageNotFoundException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import dblab.sharing_flatform.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Transactional
    public MessageResponseDto sendMessage(MessageRequestDto messageRequestDto) {
        Message message = new Message(messageRequestDto.getContent(),
                memberRepository.findByUsername(messageRequestDto.getReceiveMember()).orElseThrow(MemberNotFoundException::new),
                memberRepository.findByUsername(messageRequestDto.getSendMember()).orElseThrow(MemberNotFoundException::new));

        messageRepository.save(message);
        return MessageResponseDto.toDto(message);
    }

    public List<MessageResponseDto> findSendMessage(String senderName){
        List<Message> messages = messageRepository.findAllBySendMember(senderName);

        return messages.stream().map(message-> MessageResponseDto.toDto(message)).collect(Collectors.toList());
    }

    public List<MessageResponseDto> findReceiveMessage(String ReceiverName){
        List<Message> messages = messageRepository.findAllByReceiverMember(ReceiverName);

        return messages.stream().map(message -> MessageResponseDto.toDto(message)).collect(Collectors.toList());

    }

    public List<MessageResponseDto> findSendMessageToMember(String senderName, Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        List<Message> allBySendMemberToMember = messageRepository.findAllBySendAndReceiverMembers(senderName, member.getUsername());

        return allBySendMemberToMember.stream().map(message -> MessageResponseDto.toDto(message)).collect(Collectors.toList());
    }

    public List<MessageResponseDto> findReceiveMessageByMember(String receiverName, Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        List<Message> allBySendMemberToMember = messageRepository.findAllBySendAndReceiverMembers(member.getUsername(), receiverName);

        return allBySendMemberToMember.stream().map(message -> MessageResponseDto.toDto(message)).collect(Collectors.toList());
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
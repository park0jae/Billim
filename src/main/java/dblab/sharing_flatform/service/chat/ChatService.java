package dblab.sharing_flatform.service.chat;

import dblab.sharing_flatform.domain.chat.ChatMessage;
import dblab.sharing_flatform.domain.chat.ChatRoom;
import dblab.sharing_flatform.dto.chat.chatMessage.ChatMessageRequestDto;
import dblab.sharing_flatform.dto.chat.chatMessage.ChatMessageDetailDto;
import dblab.sharing_flatform.exception.chat.ChatRoomNotFoundException;
import dblab.sharing_flatform.repository.chat.ChatMessageRepository;
import dblab.sharing_flatform.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatService {

    private ChatMessageRepository chatMessageRepository;
    private ChatRoomRepository chatRoomRepository;

    public List<ChatMessageDetailDto> findAllChatByRoomId(String id, ChatMessageDetailDto dto){
        List<ChatMessage> messages = chatMessageRepository.findAllByRoomId(id);

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(id).orElseThrow(ChatRoomNotFoundException::new);
        ChatMessageRequestDto chatMessageRequestDto = new ChatMessageRequestDto(
                dto.getRoomId(), dto.getWriter(), dto.getMessage()
        );
        chatMessageRepository.save(ChatMessage.toEntity(chatMessageRequestDto, chatRoom));

        return messages.stream().map(m -> ChatMessageDetailDto.toDto(m)).collect(Collectors.toList());
    }

    @Transactional
    public void save(ChatMessageDetailDto message) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(message.getRoomId()).orElseThrow(ChatRoomNotFoundException::new);
        ChatMessageRequestDto requestDto = new ChatMessageRequestDto(message.getRoomId(), message.getWriter(), message.getMessage());

        chatMessageRepository.save(ChatMessage.toEntity(requestDto, chatRoom));
    }
}

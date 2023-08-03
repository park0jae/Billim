package dblab.sharing_flatform.controller.chat;

import dblab.sharing_flatform.dto.chat.chatMessage.ChatMessageDetailDto;
import dblab.sharing_flatform.repository.chat.ChatMessageRepository;
import dblab.sharing_flatform.repository.chat.ChatRoomRepository;
import dblab.sharing_flatform.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    //Client 가 SEND 할 수 있는 경로
    //stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDetailDto message) {
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");

        List<ChatMessageDetailDto> chatList = chatService.findAllChatByRoomId(message.getRoomId(), message);

        if (chatList != null) {
            for (ChatMessageDetailDto c : chatList) {
                message.setWriter(c.getWriter());
                message.setMessage(c.getMessage());
            }
        }
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDetailDto message) {
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

        chatService.save(message);
    }
}
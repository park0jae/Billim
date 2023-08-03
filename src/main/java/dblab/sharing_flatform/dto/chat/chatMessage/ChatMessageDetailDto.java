package dblab.sharing_flatform.dto.chat.chatMessage;

import dblab.sharing_flatform.domain.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDetailDto {

    private Long chatId;
    private Long chatRoomId;

    private String roomId;
    private String writer;
    private String message;

    public static ChatMessageDetailDto toDto(ChatMessage chatMessage){
        return  new ChatMessageDetailDto(
                chatMessage.getId(),
                chatMessage.getChatRoom().getId(),
                chatMessage.getChatRoom().getRoomId(),
                chatMessage.getWriter(),
                chatMessage.getMessage()
        );
    }
}

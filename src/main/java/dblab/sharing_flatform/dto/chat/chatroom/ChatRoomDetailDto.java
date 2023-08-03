package dblab.sharing_flatform.dto.chat.chatroom;

import dblab.sharing_flatform.domain.chat.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDto {
    private Long chatRoomId;
    private String chatMentor;
    private String roomId;
    private String name;

    public static ChatRoomDetailDto toDto(ChatRoom chatRoom){
        return new ChatRoomDetailDto(
                chatRoom.getId(),
                chatRoom.getChatMentor(),
                chatRoom.getRoomId(),
                chatRoom.getRoomName());
    }

}

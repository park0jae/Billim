package dblab.sharing_flatform.dto.chat.chatroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequestDto {

    private String chatMentor;
    private String roomId;
    private String name;

    private Set<WebSocketSession> sessions = new HashSet<>();
    //WebSocketSession 은 Spring 에서 Websocket Connection 이 맺어진 세션

    public static ChatRoomRequestDto create(String name){
        ChatRoomRequestDto room = new ChatRoomRequestDto();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}

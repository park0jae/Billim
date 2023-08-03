package dblab.sharing_flatform.dto.chat.chatMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {

    private String roomId;
    private String writer;
    private String message;

}

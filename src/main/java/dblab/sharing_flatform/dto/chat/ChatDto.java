package dblab.sharing_flatform.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {
    public enum ChatType{
        ENTER, COMM
    }

    private ChatType chatType;
    private String roomId;
    private String sender;
    private String content;

}

package dblab.sharing_platform.dto.message;

import dblab.sharing_platform.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long id;
    private String content;
    private String senderNickname;
    private String recevierNickname;

    public static MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getSendMember().getNickname(),
                message.getReceiveMember().getNickname());
    }
}

package dblab.sharing_flatform.dto.message;

import dblab.sharing_flatform.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long id;
    private String content;
    private String sendMember;
    private String receiverMember;

    public static MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getSendMember().getUsername(),
                message.getReceiveMember().getUsername());
    }
}

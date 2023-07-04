package dblab.sharing_flatform.dto.message;

import dblab.sharing_flatform.domain.member.Member;
import dblab.sharing_flatform.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    private Long id;
    private String content;
    private Member receiverMember;
    private Member sendMember;

    public static MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getId(),
                message.getContent(),
                message.getSendMember(),
                message.getReceiveMember());
    }
}

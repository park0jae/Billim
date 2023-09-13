package dblab.sharing_platform.dto.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import dblab.sharing_platform.domain.message.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDto {

    private Long id;
    private String content;
    private String senderNickname;
    private String receiverNickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    private boolean checked;

    public static MessageDto toDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getSendMember().getNickname(),
                message.getReceiveMember().getNickname(),
                message.getCreatedTime(),
                message.isChecked());
    }
}

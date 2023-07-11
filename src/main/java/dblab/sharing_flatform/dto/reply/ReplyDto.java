package dblab.sharing_flatform.dto.reply;

import dblab.sharing_flatform.domain.reply.Reply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyDto {

    private String username;
    private String content;

    public static List<ReplyDto> toDtoList(List<Reply> replies) {
        return replies.stream().map(r -> new ReplyDto(r.getMember().getUsername(), r.getContent())).collect(Collectors.toList());
    }
}

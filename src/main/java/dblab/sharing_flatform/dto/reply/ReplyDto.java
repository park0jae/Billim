package dblab.sharing_flatform.dto.reply;

import com.fasterxml.jackson.annotation.JsonFormat;
import dblab.sharing_flatform.domain.reply.Reply;
import dblab.sharing_flatform.helper.FlatListToHierarchicalHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyDto {

    private String username;
    private String content;

    @ApiModelProperty(value = "자식 댓글 리스트")
    private List<ReplyDto> children;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    public static List<ReplyDto> toDtoList(List<Reply> replies) {
        FlatListToHierarchicalHelper helper = FlatListToHierarchicalHelper.newInstance(
                replies,
                r -> ReplyDto.toDto(r),
                r -> r.getParent(),
                r -> r.getId(),
                d -> d.getChildren());
        return helper.convert();
    }

    public static ReplyDto toDto(Reply reply) {
        return new ReplyDto(reply.getMember() == null ? "(알수없음)" : reply.getMember().getUsername(),
                        reply.getContent(),
                        new ArrayList<>(),
                        reply.getCreatedTime());
    }
}

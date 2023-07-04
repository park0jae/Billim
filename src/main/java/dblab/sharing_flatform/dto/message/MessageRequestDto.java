package dblab.sharing_flatform.dto.message;

import dblab.sharing_flatform.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {

    private String content;

    @Null
    private String sendMember;

    private String receiveMember;

    private boolean deleteBySender;
    private boolean deleteByReceiver;

}

package dblab.sharing_flatform.dto.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dblab.sharing_flatform.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {

    private String content;

    @Nullable
    private String sendMember;

    private String receiveMember;

}

package dblab.sharing_flatform.dto.message.crud.create;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@ApiModel(value = "메세지 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequestDto {

    @ApiModelProperty(value = "메세지 내용", notes = "메세지 내용을 입력해주세요.", required = true)
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "수신자 닉네임", notes = "수신자 닉네임을 입력해주세요", required = true)
    @NotBlank(message = "수신자 닉네임을 입력해주세요.")
    private String receiveMember;

}

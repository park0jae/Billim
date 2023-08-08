package dblab.sharing_platform.dto.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class EmailAuthRequest {

    @ApiModelProperty(value = "username", notes = "아이디는 이메일 형식으로 입력해주세요", required = true, example = "kimdongwoong@naver.com")
    @NotBlank(message = "이메일을를 입력해주세요.")
    private String username;

    @ApiModelProperty(value = "EmailAuthKey", notes = "인증번호를 입력해주세요.", required = true, example = "I35l2W4")
    @NotBlank(message = "인증번호를 입력해주세요.")
    private String authKey;
}

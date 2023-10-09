package dblab.sharing_platform.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "비밀번호 재설정 요청")
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetRequestDto extends EmailAuthRequest {

    @ApiModelProperty(value = "password", notes = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.", required = true, example = "123456a!")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    @NotBlank(message = "재설정할 비밀번호를 입력해주세요.")
    private String password;

    @ApiModelProperty(value = "verifyPassword", notes = "password를 확인합니다.", required = true, example = "kimdongwoong@naver.com")
    @NotBlank(message = "재설정할 비밀번호를 다시 한 번 입력해주세요.")
    private String verifyPassword;
}

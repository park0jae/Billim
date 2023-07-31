package dblab.sharing_flatform.dto.member.crud.create;

import dblab.sharing_flatform.domain.embedded.address.Address;
import dblab.sharing_flatform.dto.member.crud.update.EmailAuthRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value = "회원가입 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateRequestDto extends EmailAuthRequest {

    @ApiModelProperty(value = "nickname", notes = "변경할 닉네임을 입력해주세요")
    @Length(min = 2, max = 15, message = "닉네임은 최소 2자, 최대 15자로 설정할 수 있습니다.")
    private String nickname;

    @ApiModelProperty(value = "password", notes = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.", required = true, example = "123456a!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;


    @ApiModelProperty(value = "phoneNumber", notes = "전화번호를 입력해주세요.", required = true, example = "01012345678")
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    @ApiModelProperty(value = "address", notes = "", required = true)
    @NotNull(message = "주소는 반드시 입력해야 합니다.")
    @Embedded
    private Address address;
}

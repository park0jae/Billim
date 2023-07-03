package dblab.sharing_flatform.dto.member;

import dblab.sharing_flatform.domain.address.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequestDto {

    @ApiModelProperty(value = "username", notes = "사용자 이름은 한글 또는 알파벳으로 입력해주세요.", required = true, example = "kimdongwoong")
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min=2, message = "사용자 이름이 너무 짧습니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 입력해주세요.")
    private String username;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @NotBlank(message = "변경할 비밀번호를 입력해주세요.")
    private String password;


    @NotBlank(message = "변경할 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @ApiModelProperty(value = "address", notes = "", required = true)
    @NotNull(message = "주소는 반드시 입력해야 합니다.")
    @Embedded
    private Address address;



}

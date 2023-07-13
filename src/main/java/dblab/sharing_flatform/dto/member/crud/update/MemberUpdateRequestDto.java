package dblab.sharing_flatform.dto.member.crud.update;

import dblab.sharing_flatform.domain.address.Address;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "회원 정보 수정 요청")
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequestDto {

    @ApiModelProperty(value = "password", notes = "변경할 Password를 입력해주세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @ApiModelProperty(value = "phoneNumber", notes = "변경할 PhoneNumber를 입력해주세요")
    private String phoneNumber;

    @ApiModelProperty(value = "address", notes = "")
    @Embedded
    private Address address;

}

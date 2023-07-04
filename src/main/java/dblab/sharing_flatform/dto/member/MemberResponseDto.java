package dblab.sharing_flatform.dto.member;

import dblab.sharing_flatform.domain.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@ApiModel(value = "회원 정보 수정 응답")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    @ApiModelProperty(value = "id", notes = "회원정보 수정 응답 id", required = true)
    private Long id;

    @ApiModelProperty(value = "username", notes = "회원정보 수정 응답 username", required = true)
    private String username;

    @ApiModelProperty(value = "password", notes = "회원정보 수정 응답 password", required = true)
    private String password;

    @ApiModelProperty(value = "phoneNum", notes = "회원정보 수정 응답 phoneNumber", required = true)
    private String phoneNumber;

    public static MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getPhoneNumber());
    }
}
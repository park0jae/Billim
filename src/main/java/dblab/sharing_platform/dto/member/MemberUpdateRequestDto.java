package dblab.sharing_platform.dto.member;

import dblab.sharing_platform.domain.embedded.address.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Embedded;

@ApiModel(value = "회원 정보 수정 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequestDto {

    @ApiModelProperty(value = "nickname", notes = "변경할 닉네임을 입력해주세요")
    @Length(min = 2, max = 15, message = "닉네임은 최소 2자, 최대 15자로 설정할 수 있습니다.")
    private String nickname;

    @ApiModelProperty(value = "phoneNumber", notes = "변경할 휴대폰 번호를 입력해주세요")
    private String phoneNumber;

    @ApiModelProperty(value = "address", notes = "변경할 주소를 입력하세요.")
    @Embedded
    private Address address;

    @ApiModelProperty(value = "introduce", notes = "자기 소개를 입력하세요. (선택)")
    private String introduce;

    @ApiModelProperty(value = "image", notes = "프로필 사진을 업로드하세요. (선택)")
    private MultipartFile image;

//    public static MemberUpdateRequestDto toDto(Member member) {
//        return new MemberUpdateRequestDto(member.getPassword(),
//                                member.getNickname(), member.getPhoneNumber(), member.getAddress(), member.getIntroduce(), )
//    }
}

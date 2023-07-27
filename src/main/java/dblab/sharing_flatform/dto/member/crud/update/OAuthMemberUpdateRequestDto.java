package dblab.sharing_flatform.dto.member.crud.update;

import dblab.sharing_flatform.domain.embedded.address.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthMemberUpdateRequestDto {

    @ApiModelProperty(hidden = true)
    @Null
    private String username;

    @ApiModelProperty(value = "nickname", notes = "변경할 닉네임을 입력해주세요")
    @Length(min = 2, max = 15, message = "닉네임은 최소 2자, 최대 15자로 설정할 수 있습니다.")
    private String nickname;

    @ApiModelProperty(value = "phoneNumber", notes = "PhoneNumber를 입력해주세요", required = true)
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @ApiModelProperty(value = "address", notes = "", required = true)
    @NotNull(message = "주소는 반드시 입력해야 합니다.")
    @Embedded
    private Address address;

    @ApiModelProperty(value = "introduce", notes = "자기 소개를 입력하세요. (선택)")
    private String introduce;

    @ApiModelProperty(value = "image", notes = "프로필 사진을 업로드하세요. (선택)")
    private MultipartFile image;

}

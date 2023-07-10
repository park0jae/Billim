package dblab.sharing_flatform.dto.member.crud.create.OAuth2MemberCreateRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2MemberCreateRequestDto {

    private String email;
    private String provider;
    private String accessToken;
}

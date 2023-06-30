package dblab.sharing_flatform.dto.member;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

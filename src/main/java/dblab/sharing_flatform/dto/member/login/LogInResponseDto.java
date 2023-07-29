package dblab.sharing_flatform.dto.member.login;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel(value = "로그인 응답")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LogInResponseDto {

    private String accessToken;
    private String refreshToken;
    public static LogInResponseDto toDto(String accessToken, String refreshToken) {
        return new LogInResponseDto(accessToken, refreshToken);
    }
}

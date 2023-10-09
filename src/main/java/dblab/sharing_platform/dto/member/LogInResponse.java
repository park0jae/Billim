package dblab.sharing_platform.dto.member;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel(value = "로그인 응답")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LogInResponse {

    private String accessToken;
    private String refreshToken;

    public static LogInResponse toDto(String accessToken, String refreshToken) {
        return new LogInResponse(accessToken, refreshToken);
    }
}

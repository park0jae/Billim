package dblab.sharing_flatform.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LogInResponseDto {

    private String token;
    public static LogInResponseDto toDto(String token) {
        return new LogInResponseDto(token);
    }
}

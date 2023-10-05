package dblab.sharing_platform.dto.oauth;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class NaverProfile {

    private NaverAccount response;

    @Getter
    @ToString
    public static class NaverAccount {
        private String email;
    }
}
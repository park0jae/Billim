package dblab.sharing_flatform.dto.oauth.crud.create;

import lombok.Getter;
import lombok.ToString;


@Getter
public class KakaoProfile extends Profile{

    private Long id;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    @ToString
    public static class KakaoAccount {
        private String email;
    }

    @Getter
    @ToString
    public static class Properties {
        private String nickname;
    }

}

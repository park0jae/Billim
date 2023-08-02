package dblab.sharing_flatform.dto.oauth;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class NaverProfile {

    // result =
    // {
    // "resultcode":"00",
    // "message":"success",
    // "response":
    //          {"id":"eexblFHOM1ekD150QA1ciaARzdun4b5hs5bmAkuJztc",
    //           "email":"rlaehddnd0422@naver.com",
    //           "mobile":"010-6609-6301",
    //           "mobile_e164":"+821066096301",
    //           "name":"\uae40\ub3d9\uc6c5"}
    // }

    private NaverAccount response;

    @Getter
    @ToString
    public static class NaverAccount {
        private String email;
    }
}
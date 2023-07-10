package dblab.sharing_flatform.controller.oauth;

import com.google.gson.Gson;
import dblab.sharing_flatform.dto.oauth.crud.create.KakaoProfile;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Controller
@RequiredArgsConstructor
//@RestController
public class OauthController {

    private final OAuthService oAuthService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/oauth2/callback/kakao")
    public @ResponseBody String login(@RequestParam String code){

        String kakaoAccessToken = oAuthService.getKakaoAccessToken(code);
        log.info("kakaoAccessToken={}", kakaoAccessToken);

        KakaoProfile kakaoUserInfo = oAuthService.getKakaoUserInfo(kakaoAccessToken);
        return "카카오 인증 성공";
    }

}

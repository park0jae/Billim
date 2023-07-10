package dblab.sharing_flatform.controller.oauth;

import dblab.sharing_flatform.dto.member.MemberPrivateDto;
import dblab.sharing_flatform.dto.member.crud.create.OAuth2MemberCreateRequestDto.OAuth2MemberCreateRequestDto;
import dblab.sharing_flatform.dto.member.login.LogInResponseDto;
import dblab.sharing_flatform.dto.oauth.crud.create.KakaoProfile;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.service.member.MemberService;
import dblab.sharing_flatform.service.member.SignService;
import dblab.sharing_flatform.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@Controller
@RequiredArgsConstructor
//@RestController
public class OauthController {

    private final OAuthService oAuthService;
    private final SignService signService;
    private final MemberService memberService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/oauth2/callback/kakao")
    public Response oauth2Login(@RequestParam String code) {
        String kakaoAccessToken = oAuthService.getKakaoAccessToken(code);
        KakaoProfile kakaoUserInfo = oAuthService.getKakaoUserInfo(kakaoAccessToken);

        OAuth2MemberCreateRequestDto req = new OAuth2MemberCreateRequestDto(kakaoUserInfo.getKakao_account().getEmail(), "kakao", kakaoAccessToken);

        try {
            memberService.getMemberInfoByUsername(req.getEmail());
        } catch (MemberNotFoundException e) {
            signService.oAuth2Signup(req);
        } finally {
            LogInResponseDto logInResponseDto = signService.oauth2Login(req);
            log.info("token = ", logInResponseDto.getToken());
            return Response.success(logInResponseDto);
        }
    }
}

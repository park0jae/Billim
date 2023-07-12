package dblab.sharing_flatform.controller.oauth;

import dblab.sharing_flatform.dto.member.crud.create.OAuth2MemberCreateRequestDto.OAuth2MemberCreateRequestDto;
import dblab.sharing_flatform.dto.member.login.LogInResponseDto;
import dblab.sharing_flatform.dto.oauth.crud.create.GoogleProfile;
import dblab.sharing_flatform.dto.oauth.crud.create.KakaoProfile;
import dblab.sharing_flatform.dto.oauth.crud.create.NaverProfile;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.oauth.OAuthUserNotFoundException;
import dblab.sharing_flatform.exception.oauth.SocialAgreementException;
import dblab.sharing_flatform.service.member.MemberService;
import dblab.sharing_flatform.service.member.SignService;
import dblab.sharing_flatform.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


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

        if(kakaoUserInfo == null) throw new OAuthUserNotFoundException();
        if(kakaoUserInfo.getKakao_account().getEmail() == null){
            oAuthService.unlinkOAuthService(kakaoAccessToken, "kakao");
            throw new SocialAgreementException();
        }
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

    @ResponseBody
    @GetMapping("/oauth2/callback/google")
    public Response oauth2LoginGoogle(@RequestParam String code) {
        String googleAccessToken = oAuthService.getGoogleAccessToken(code);
        GoogleProfile googleUserInfo = oAuthService.getGoogleUserInfo(googleAccessToken);

        if(googleUserInfo == null) throw new OAuthUserNotFoundException();
        if(googleUserInfo.getEmail() == null){
            oAuthService.unlinkOAuthService(googleAccessToken, "google");
            throw new SocialAgreementException();
        }

        OAuth2MemberCreateRequestDto google = new OAuth2MemberCreateRequestDto(googleUserInfo.getEmail(), "google", googleAccessToken);

        try {
            memberService.getMemberInfoByUsername(google.getEmail());
        } catch (MemberNotFoundException e) {

            signService.oAuth2Signup(google);
        } finally {
            LogInResponseDto logInResponseDto = signService.oauth2Login(google);
            log.info("token = ", logInResponseDto.getToken());
            return Response.success(logInResponseDto);
        }
    }

    @ResponseBody
    @GetMapping("/oauth2/callback/naver")
    public Response oauth2LoginNaver(@RequestParam String code) {
        String naverAccessToken = oAuthService.getNaverAccessToken(code);
        NaverProfile naverUserInfo = oAuthService.getNaverUserInfo(naverAccessToken);

        if(naverUserInfo == null) throw new OAuthUserNotFoundException();
        if(naverUserInfo.getResponse().getEmail() == null){
            oAuthService.unlinkOAuthService(naverAccessToken, "naver");
            throw new SocialAgreementException();
        }
        OAuth2MemberCreateRequestDto req = new OAuth2MemberCreateRequestDto(naverUserInfo.getResponse().getEmail(), "naver", naverAccessToken);

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

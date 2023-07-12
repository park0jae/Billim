//package dblab.sharing_flatform.controller.oauth;
//
//import dblab.sharing_flatform.dto.member.crud.create.OAuth2MemberCreateRequestDto.OAuth2MemberCreateRequestDto;
//import dblab.sharing_flatform.dto.member.login.LogInResponseDto;
//import dblab.sharing_flatform.dto.oauth.crud.create.AccessTokenRequestDto;
//import dblab.sharing_flatform.dto.oauth.crud.create.GoogleProfile;
//import dblab.sharing_flatform.dto.oauth.crud.create.KakaoProfile;
//import dblab.sharing_flatform.dto.oauth.crud.create.NaverProfile;
//import dblab.sharing_flatform.dto.response.Response;
//import dblab.sharing_flatform.exception.member.MemberNotFoundException;
//import dblab.sharing_flatform.exception.oauth.OAuthUserNotFoundException;
//import dblab.sharing_flatform.exception.oauth.SocialAgreementException;
//import dblab.sharing_flatform.service.member.MemberService;
//import dblab.sharing_flatform.service.member.SignService;
//import dblab.sharing_flatform.service.oauth.OAuthService;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//
//import org.springframework.web.bind.annotation.*;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
////@RestController
//public class OauthController {
//
//    private final OAuthService oAuthService;
//    private final SignService signService;
//    private final MemberService memberService;
//
//    @ApiOperation(value = "OAuth2.0 메인 페이지", notes = "OAuth2.0 로그인을 위한 메인 페이지입니다.")
//    @GetMapping("/")
//    public String indeㅁx() {
//        return "index";
//    }
//
//    @ApiOperation(value = "카카오 로그인", notes = "OAuth2.0 카카오로 소셜 로그인을 진행합니다.")
//    @ResponseBody
//    @GetMapping("/oauth2/callback/kakao")
//    public Response oauth2Login(@RequestParam String code,
//                                @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
//                                @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
//                                @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri
//    ) {
//        AccessTokenRequestDto accessTokenRequestDto= new AccessTokenRequestDto(clientId, clientSecret, redirectUri);
//
//        String kakaoAccessToken = oAuthService.getAccessToken(code, "kakao", accessTokenRequestDto);
//        LogInResponseDto logInResponseDto = oauthLogin("kakao", kakaoAccessToken);
//
//        return Response.success(logInResponseDto);
//    }
//
//    @ApiOperation(value = "구글 로그인", notes = "OAuth2.0 구글로 소셜 로그인을 진행합니다.")
//    @ResponseBody
//    @GetMapping("/oauth2/callback/google")
//    public Response oauth2LoginGoogle(@RequestParam String code,
//                                      @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
//                                      @Value("${spring.security.oauth2.client.registration.google.client-secret}") String clientSecret,
//                                      @Value("${spring.security.oauth2.client.registration.google.redirect-uri}") String redirectUri
//    ) {
//        AccessTokenRequestDto accessTokenRequestDto= new AccessTokenRequestDto(clientId, clientSecret, redirectUri);
//        String googleAccessToken = oAuthService.getAccessToken(code, "google", accessTokenRequestDto);
//        LogInResponseDto logInResponseDto = oauthLogin("google", googleAccessToken);
//
//        return Response.success(logInResponseDto);
//    }
//
//    @ApiOperation(value = "네이버 로그인", notes = "OAuth2.0 네이버로 소셜 로그인을 진행합니다.")
//    @ResponseBody
//    @GetMapping("/oauth2/callback/naver")
//    public Response oauth2LoginNaver(@RequestParam String code,
//                                     @Value("${spring.security.oauth2.client.registration.naver.client-id}") String clientId,
//                                     @Value("${spring.security.oauth2.client.registration.naver.client-secret}") String clientSecret,
//                                     @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}") String redirectUri
//     ) {
//        AccessTokenRequestDto accessTokenRequestDto= new AccessTokenRequestDto(clientId, clientSecret, redirectUri);
//
//        String naverAccessToken = oAuthService.getAccessToken(code, "naver", accessTokenRequestDto);
//        LogInResponseDto logInResponseDto = oauthLogin("naver", naverAccessToken);
//
//        return Response.success(logInResponseDto);
//    }
//
//    private LogInResponseDto oauthLogin(String provider, String accessToken){
//        String email = "";
//        switch (provider){
//            case "kakao":
//                KakaoProfile oAuthKakaoInfo = (KakaoProfile) oAuthService.getOAuthUserInfo(accessToken, provider);
//                if(oAuthKakaoInfo == null) throw new OAuthUserNotFoundException();
//                email = oAuthKakaoInfo.getKakao_account().getEmail();
//                break;
//            case "google" :
//                GoogleProfile oAuthGoogleInfo = (GoogleProfile) oAuthService.getOAuthUserInfo(accessToken, provider);
//                if(oAuthGoogleInfo == null) throw new OAuthUserNotFoundException();
//                email = oAuthGoogleInfo.getEmail();
//                break;
//            case "naver":
//                NaverProfile oAuthNaverInfo = (NaverProfile) oAuthService.getOAuthUserInfo(accessToken, provider);
//                if(oAuthNaverInfo == null) throw new OAuthUserNotFoundException();
//                email = oAuthNaverInfo.getResponse().getEmail();
//                break;
//        }
//
//        if(email == null){
//            oAuthService.unlinkOAuthService(accessToken, provider);
//            throw new SocialAgreementException();
//        }
//        OAuth2MemberCreateRequestDto req = new OAuth2MemberCreateRequestDto(email, provider, accessToken);
////
//        try {
//            memberService.getMemberInfoByUsername(req.getEmail());
//        } catch (MemberNotFoundException e) {
//            signService.oAuth2Signup(req);
//        } finally {
//            LogInResponseDto logInResponseDto = signService.oauth2Login(req);
//            log.info("token = ", logInResponseDto.getToken());
//            return logInResponseDto;
//        }
//    }
//
//
//}

package dblab.sharing_flatform.controller.member;

import dblab.sharing_flatform.dto.member.crud.create.OAuth2MemberCreateRequestDto.OAuth2MemberCreateRequestDto;
import dblab.sharing_flatform.dto.member.login.LogInResponseDto;
import dblab.sharing_flatform.dto.member.login.LoginRequestDto;
import dblab.sharing_flatform.dto.member.crud.create.MemberCreateRequestDto;
import dblab.sharing_flatform.dto.oauth.crud.create.AccessTokenRequestDto;
import dblab.sharing_flatform.dto.response.Response;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.oauth.OAuthUserNotFoundException;
import dblab.sharing_flatform.exception.oauth.SocialAgreementException;
import dblab.sharing_flatform.service.mail.NaverMailService;
import dblab.sharing_flatform.service.member.MemberService;
import dblab.sharing_flatform.service.member.SignService;
import dblab.sharing_flatform.service.oauth.OAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;


@Api(value = "Sign Controller", tags = "Sign")
@Slf4j
@Controller
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    private final MemberService memberService;
    private final OAuthService oAuthService;
    private final NaverMailService naverMailService;

    @ApiOperation(value = "일반 회원가입", notes = "일반 회원가입을 한다.") // 2
    @PostMapping("/sign-up")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response signup(@Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto){
        signService.signUp(memberCreateRequestDto);
        return Response.success();
    }

    @ApiOperation(value = "일반 로그인", notes = "일반 로그인을 한다.") // 2
    @PostMapping("/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        LogInResponseDto logInResponseDto = signService.login(loginRequestDto);
        return Response.success(logInResponseDto);
    }

    @ApiOperation(value = "OAuth2.0 메인 페이지", notes = "OAuth2.0 로그인을 위한 메인 페이지입니다.")
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @ApiOperation(value = "이메일 인증" , notes = "회원가입 시 이메일 인증을 위한 엔드포인트")
    @ResponseBody
    @PostMapping("/mail")
    public Response mailConfirm(@RequestParam(name = "email") String email)  {
        String code = naverMailService.sendSimpleMessage(email);
        return Response.success(code);
    }

    @ApiOperation(value = "카카오 로그인", notes = "OAuth2.0 카카오로 소셜 로그인을 진행합니다.")
    @ResponseBody
    @GetMapping("/oauth2/callback/kakao")
    public Response oauth2Login(@RequestParam String code,
                                @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                                @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
                                @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri) {
        AccessTokenRequestDto accessTokenRequestDto= new AccessTokenRequestDto(clientId, clientSecret, redirectUri);
        OAuth2MemberCreateRequestDto req = oAuthService.getAccessToken(code, "kakao", accessTokenRequestDto);
        LogInResponseDto logInResponseDto = signUpAndLogin(req);
        return Response.success(logInResponseDto);
    }


    @ApiOperation(value = "구글 로그인", notes = "OAuth2.0 구글로 소셜 로그인을 진행합니다.")
    @ResponseBody
    @GetMapping("/oauth2/callback/google")
    public Response oauth2LoginGoogle(@RequestParam String code,
                                      @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
                                      @Value("${spring.security.oauth2.client.registration.google.client-secret}") String clientSecret,
                                      @Value("${spring.security.oauth2.client.registration.google.redirect-uri}") String redirectUri) {
        AccessTokenRequestDto accessTokenRequestDto= new AccessTokenRequestDto(clientId, clientSecret, redirectUri);
        OAuth2MemberCreateRequestDto req = oAuthService.getAccessToken(code, "google", accessTokenRequestDto);
        LogInResponseDto logInResponseDto = signUpAndLogin(req);
        return Response.success(logInResponseDto);
    }

    @ApiOperation(value = "네이버 로그인", notes = "OAuth2.0 네이버로 소셜 로그인을 진행합니다.")
    @ResponseBody
    @GetMapping("/oauth2/callback/naver")
    public Response oauth2LoginNaver(@RequestParam String code,
                                     @Value("${spring.security.oauth2.client.registration.naver.client-id}") String clientId,
                                     @Value("${spring.security.oauth2.client.registration.naver.client-secret}") String clientSecret,
                                     @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}") String redirectUri) {
        AccessTokenRequestDto accessTokenRequestDto = new AccessTokenRequestDto(clientId, clientSecret, redirectUri);
        OAuth2MemberCreateRequestDto req = oAuthService.getAccessToken(code, "naver", accessTokenRequestDto);
        LogInResponseDto logInResponseDto = signUpAndLogin(req);
        return Response.success(logInResponseDto);
    }

    private LogInResponseDto signUpAndLogin(OAuth2MemberCreateRequestDto req) {
        try {
            memberService.readMyInfo(req.getEmail());
        } catch (MemberNotFoundException e) {
            signService.oAuth2Signup(req);
        } finally {
            LogInResponseDto logInResponseDto = signService.oauth2Login(req);
            return logInResponseDto;
        }
    }


}

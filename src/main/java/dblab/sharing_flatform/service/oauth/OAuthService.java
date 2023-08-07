package dblab.sharing_flatform.service.oauth;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dblab.sharing_flatform.dto.member.OAuth2MemberCreateRequestDto;
import dblab.sharing_flatform.dto.oauth.AccessTokenRequestDto;
import dblab.sharing_flatform.dto.oauth.GoogleProfile;
import dblab.sharing_flatform.dto.oauth.KakaoProfile;
import dblab.sharing_flatform.dto.oauth.NaverProfile;
import dblab.sharing_flatform.exception.auth.AlreadyExistsMemberException;
import dblab.sharing_flatform.exception.oauth.OAuthCommunicationException;
import dblab.sharing_flatform.exception.oauth.OAuthUserNotFoundException;
import dblab.sharing_flatform.exception.oauth.SocialAgreementException;
import dblab.sharing_flatform.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static dblab.sharing_flatform.config.oauth.provider.OAuthInfo.*;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final Gson gson;
    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private static final String NONE = "None";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String POST = "POST";
    private static final String GET = "GET";

    public OAuth2MemberCreateRequestDto getAccessToken(String code, String provider, AccessTokenRequestDto accessTokenRequestDto){
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "";

        switch (provider){
            case KAKAO:
                reqURL = ACCESS_TOKEN_URL_KAKAO;
                break;
            case GOOGLE:
                reqURL = ACCESS_TOKEN_URL_GOOGLE;
                break;
            case NAVER:
                reqURL = ACCESS_TOKEN_URL_NAVER;
                break;
        }

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod(POST);
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append(GRANT_TYPE);
            sb.append(CLIENT_ID+ accessTokenRequestDto.getClientId()); // TODO REST_API_KEY 입력
            sb.append(CLIENT_SECRET+ accessTokenRequestDto.getClientSecret());
            sb.append(REDIRECT_URI + accessTokenRequestDto.getRedirectUri()); // TODO 인가코드 받은 redirect_uri 입력
            sb.append(CODE + code);
            bw.write(sb.toString());
            bw.flush();

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonElement element = JsonParser.parseString(result);
            access_Token = element.getAsJsonObject().get(ACCESS_TOKEN).getAsString();

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return getProvideInfo(provider, access_Token);
    }

    public Object getOAuthUserInfo(String accessToken, String provider)  {

        String reqURL = "";

        switch (provider){
            case KAKAO:
                reqURL = USER_INFO_URL_KAKAO;
                break;
            case GOOGLE:
                reqURL = USER_INFO_URL_GOOGLE;
                break;
            case NAVER:
                reqURL = USER_INFO_URL_NAVER;
                break;
        }

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(GET);
            conn.setDoOutput(true);
            conn.setRequestProperty(AUTHORIZATION, BEARER + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
//            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            System.out.println("response body : " + result);

            if(provider == KAKAO){
                KakaoProfile kakaoProfile = gson.fromJson(result, KakaoProfile.class);
                br.close();
                return kakaoProfile;
            }else if(provider == GOOGLE){
                GoogleProfile googleProfile = gson.fromJson(result, GoogleProfile.class);
                br.close();
                return googleProfile;
            }else {
                NaverProfile naverProfile = gson.fromJson(result, NaverProfile.class);
                br.close();
                return naverProfile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void unlinkOAuthService(String accessToken, String provider){
        String url = "";
        switch (provider){
            case KAKAO:
                url = UNLINK_URL_KAKAO;
                break;
            case NAVER:
                url = UNLINK_URL_NAVER_FRONT + accessToken.replaceAll("'", "") + UNLINK_URL_NAVER_END;
                break;
            case GOOGLE:
                url = UNLINK_URL_GOOGLE + accessToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(AUTHORIZATION, BEARER + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if(response.getStatusCode() == HttpStatus.OK){
            return;
        }
        throw new OAuthCommunicationException();
    }


    private OAuth2MemberCreateRequestDto getProvideInfo(String provider, String accessToken){
        String email = "";
        switch (provider){
            case KAKAO:
                KakaoProfile oAuthKakaoInfo = (KakaoProfile) getOAuthUserInfo(accessToken, provider);
                if(oAuthKakaoInfo == null) throw new OAuthUserNotFoundException();
                email = oAuthKakaoInfo.getKakao_account().getEmail();
                break;
            case GOOGLE:
                GoogleProfile oAuthGoogleInfo = (GoogleProfile) getOAuthUserInfo(accessToken, provider);
                if(oAuthGoogleInfo == null) throw new OAuthUserNotFoundException();
                email = oAuthGoogleInfo.getEmail();
                break;
            case NAVER:
                NaverProfile oAuthNaverInfo = (NaverProfile) getOAuthUserInfo(accessToken, provider);
                if(oAuthNaverInfo == null) throw new OAuthUserNotFoundException();
                email = oAuthNaverInfo.getResponse().getEmail();
                break;
        }

        if(email == null){
            unlinkOAuthService(accessToken, provider);
            throw new SocialAgreementException();
        } else if (memberRepository.existsByUsernameAndProvider(email, NONE)) {
            throw new AlreadyExistsMemberException();
        }

        return new OAuth2MemberCreateRequestDto(email, provider, accessToken);
    }

}

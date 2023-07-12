package dblab.sharing_flatform.service.oauth;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import dblab.sharing_flatform.dto.oauth.crud.create.*;
import dblab.sharing_flatform.exception.oauth.OAuthCommunicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final Gson gson;
    private final RestTemplate restTemplate;

    public String getAccessToken(String code, String provider, AccessTokenRequestDto accessTokenRequestDto){
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "";

        switch (provider){
            case "kakao":
                reqURL = "https://kauth.kakao.com/oauth/token";
                break;
            case "google":
                reqURL = "https://oauth2.googleapis.com/token";
                break;
            case "naver":
                reqURL = "https://nid.naver.com/oauth2.0/token";
                break;
        }

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+ accessTokenRequestDto.getClientId()); // TODO REST_API_KEY 입력
            sb.append("&client_secret="+ accessTokenRequestDto.getClientSecret());
            sb.append("&redirect_uri=" + accessTokenRequestDto.getRedirectUri()); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

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

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            //refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

//            System.out.println("access_token : " + access_Token);
//            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public Object getOAuthUserInfo(String accessToken, String provider)  {

        String reqURL = "";

        switch (provider){
            case "kakao":
                reqURL = "https://kapi.kakao.com/v2/user/me";
                break;
            case "google":
                reqURL = "https://www.googleapis.com/oauth2/v1/userinfo";
                break;
            case "naver":
                reqURL = "https://openapi.naver.com/v1/nid/me";
                break;
        }

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

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

            if(provider == "kakao"){
                KakaoProfile kakaoProfile = gson.fromJson(result, KakaoProfile.class);
                br.close();
                return kakaoProfile;
            }else if(provider == "google"){
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
            case "kakao" :
                url = "https://kapi.kakao.com/v1/user/unlink";
                break;
            case "naver" :
                url = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=nX6jP4IgsXNpJRqbg0Q5&client_secret=1vNwHm8Yri&access_token="
                        +accessToken.replaceAll("'", "")+"&service_provider=NAVER";
                break;
            case "google" :
                url = "https://oauth2.googleapis.com/revoke?token=" + accessToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if(response.getStatusCode() == HttpStatus.OK){
            log.info("unlink = {}", response.getBody());
            return;
        }
        throw new OAuthCommunicationException();
    }
}

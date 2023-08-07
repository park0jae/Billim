package dblab.sharing_flatform.config.oauth.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import dblab.sharing_flatform.domain.embedded.address.Address;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

import static dblab.sharing_flatform.config.oauth.provider.OAuthInfo.*;

public class KakaoUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return KAKAO;
    }

    @Override
    public String getPhoneNumber(){
        return "";
    }

    @Override
    public String getEmail() {
        ObjectMapper objectMapper = new ObjectMapper();
        Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, new TypeReference<Map<String, Object>>() {});
        return (String) account.get(EMAIL);
    }
    @Override
    public String getName() {
        return (String) attributes.get(PROFILE_NICKNAME);
    }

    @Override
    public Address getAddress() {
        return new Address("","","","");
    }
}
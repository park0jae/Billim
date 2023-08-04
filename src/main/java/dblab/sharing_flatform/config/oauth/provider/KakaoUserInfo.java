package dblab.sharing_flatform.config.oauth.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import dblab.sharing_flatform.domain.embedded.address.Address;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getPhoneNumber(){
        return "";
    }

    @Override
    public String getEmail() {
        ObjectMapper objectMapper = new ObjectMapper();
        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, new TypeReference<Map<String, Object>>() {});

        return (String) account.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("profile_nickname");
    }

    @Override
    public Address getAddress() {
        return new Address("","","","");
    }
}
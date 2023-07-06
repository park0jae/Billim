package dblab.sharing_flatform.config.oauth.provider;

import dblab.sharing_flatform.domain.address.Address;

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
        return "01012345678";
    }

    @Override
    public String getEmail() {
        Map<String,Object> account = (Map<String, Object>) attributes.get("kakao_account");
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

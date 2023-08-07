package dblab.sharing_flatform.config.oauth.provider;

import dblab.sharing_flatform.domain.embedded.address.Address;

import java.util.Map;

import static dblab.sharing_flatform.config.oauth.provider.OAuthInfo.*;

public class NaverUserInfo implements OAuth2UserInfo {

    public NaverUserInfo(Map<String,Object> attributes) {
        this.attributes = attributes;
    }

    private Map<String,Object> attributes;

    @Override
    public String getProvider() {
        return NAVER;
    }

    @Override
    public String getPhoneNumber() {
        return "";
    }

    @Override
    public String getName() {
        return attributes.get(NAME).toString();
    }

    @Override
    public Address getAddress() {
        return new Address("","","","");
    }

    @Override
    public String getEmail() {
        return attributes.get(EMAIL).toString();
    }
}
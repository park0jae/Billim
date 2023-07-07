package dblab.sharing_flatform.config.oauth.provider;

import dblab.sharing_flatform.domain.address.Address;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    private Map<String, Object> attributes;


    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getPhoneNumber() {
        return "";
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public Address getAddress() {
        return new Address("","","","");
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
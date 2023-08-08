package dblab.sharing_platform.config.oauth.provider;

import dblab.sharing_platform.domain.embedded.address.Address;

import java.util.Map;

import static dblab.sharing_platform.config.oauth.provider.OAuthInfo.*;

public class GoogleUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return GOOGLE;
    }

    @Override
    public String getPhoneNumber() {
        return "";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(EMAIL);
    }

    @Override
    public String getName() {
        return (String) attributes.get(NAME);
    }

    @Override
    public Address getAddress() {
        return new Address("","","","");
    }
}

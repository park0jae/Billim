package dblab.sharing_platform.config.oauth.provider;

import dblab.sharing_platform.domain.embedded.address.Address;

public interface OAuth2UserInfo {

    String getProvider();
    String getPhoneNumber();
    String getEmail();
    String getName();
    Address getAddress();
}

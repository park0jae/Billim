package dblab.sharing_flatform.config.oauth.provider;

import dblab.sharing_flatform.domain.embedded.address.Address;

public interface OAuth2UserInfo {
    String getProvider();
    String getPhoneNumber();
    String getEmail();
    String getName();

    Address getAddress();
}

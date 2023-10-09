package dblab.sharing_platform.dto.oauth;

import lombok.Getter;

@Getter
public class AccessTokenRequest {

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public AccessTokenRequest(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}

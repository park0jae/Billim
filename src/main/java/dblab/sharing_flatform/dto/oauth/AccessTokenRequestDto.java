package dblab.sharing_flatform.dto.oauth;

import lombok.Getter;

@Getter
public class AccessTokenRequestDto {

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public AccessTokenRequestDto(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}

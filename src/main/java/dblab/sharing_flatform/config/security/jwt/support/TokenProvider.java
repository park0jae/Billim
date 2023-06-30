package dblab.sharing_flatform.config.security.jwt.support;

import dblab.sharing_flatform.config.security.jwt.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtHandler jwtHandler;

    public String createAccessToken(Authentication authentication) {
        return jwtHandler.createAccessToken(authentication);
    }

    // Token -> UsernamePasswordAuthenticationToken
    public Authentication getAuthentication(String jwt) {
        return jwtHandler.getAuthenticationFromToken(jwt);
    }

    // validate Access Token
    public boolean validate(String jwt) {
        return jwtHandler.validateToken(jwt);
    }

}

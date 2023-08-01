package dblab.sharing_flatform.config.security.jwt.filter;

import dblab.sharing_flatform.config.security.jwt.provider.TokenProvider;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dblab.sharing_flatform.config.security.jwt.provider.TokenProvider.ACCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String REFRESH_HEADER = "Auth";
    public static final String LOGIN_PATH = "/login";
    public static final String EXCEPTION_PATH = "/exception";
    public static final String SAVED_AUTHENTICATION = "인증정보를 Security Context에 저장하였습니다.";
    public static final String ACCESS_EXPIRED = "액세스 토큰이 만료되었습니다.";
    public static final String REFRESH_EXPIRED = "리프레시 토큰이 만료되었습니다.";
    public static final String ALL_EXPIRED = "다시 로그인해주세요.";


    // 인증/인가가 필요한 요청시 실행하는 필터
    // 토큰 검증 이후 -> 인증정보 Authentication 객체를 SecurityContext에 저장
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = extractTokenFromRequest(request, AUTH_HEADER);
        String refreshToken = request.getHeader(REFRESH_HEADER);

        if (request.getRequestURI().startsWith(LOGIN_PATH)) {
            chain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().startsWith(EXCEPTION_PATH)) {
            chain.doFilter(request, response);
            return;
        }

        if (validateExpire(accessToken) && validateExpire(refreshToken)) {
            log.info(SAVED_AUTHENTICATION);
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthenticationFromToken(accessToken));
        } else if (!validateExpire(accessToken) && validateExpire(refreshToken)) {
            log.info(ACCESS_EXPIRED);
            if (tokenProvider.refreshTokenValidation(refreshToken)) {
                Authentication authentication = tokenProvider.getAuthenticationFromToken(refreshToken);
                String newAccessToken = tokenProvider.createToken(authentication, ACCESS);
                Authentication newAuthentication = tokenProvider.getAuthenticationFromToken(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);

                response.setHeader(AUTH_HEADER, newAccessToken);
            }
        } else if (validateExpire(accessToken) && !validateExpire(refreshToken)) {
            log.info(REFRESH_EXPIRED);
            if (tokenProvider.validateToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthenticationFromToken(accessToken);
                tokenProvider.reIssueRefreshToken(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            log.debug(ALL_EXPIRED);
        }
        chain.doFilter(request, response);
    }


    private String extractTokenFromRequest(HttpServletRequest request, String header) {
        String Bearer_token = request.getHeader(header);
        return extractToken(Bearer_token);
    }

    private String extractToken(String bearer_token) {
        if (StringUtils.hasText(bearer_token) && bearer_token.startsWith(BEARER)) {
            return bearer_token.substring(7);
        }
        return null;
    }

    private boolean validateExpire(String token) {
        return StringUtils.hasText(token) && tokenProvider.validateExpire(token);
    }
}

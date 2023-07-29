package dblab.sharing_flatform.config.security.jwt.filter;

import dblab.sharing_flatform.config.security.jwt.provider.TokenProvider;
import dblab.sharing_flatform.config.security.util.SecurityUtil;
import dblab.sharing_flatform.domain.refresh.RefreshToken;
import dblab.sharing_flatform.exception.token.TokenNotFoundException;
import dblab.sharing_flatform.repository.refresh.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    // 인증/인가가 필요한 요청시 실행하는 필터
    // 토큰 검증 이후 -> 인증정보 Authentication 객체를 SecurityContext에 저장
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = extractTokenFromRequest(request, "Authorization");

        if (request.getRequestURI().startsWith("/login")) {
            chain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().startsWith("/exception/invalid-token")) {
            chain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            // accessToken 이 유효한 상황
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthenticationFromToken(accessToken));
            log.info("인증정보를 Security Context에 저장하였습니다.");
        }
        else if(StringUtils.hasText(accessToken) && !tokenProvider.validateToken(accessToken)){
            log.info("액세스 토큰이 만료되었습니다.");
            String refreshToken = null;


            if(StringUtils.hasText(request.getHeader("Auth"))){
                String username = request.getHeader("Auth");
                refreshToken = tokenProvider.getRefreshToken(username);
            }

            if(StringUtils.hasText(refreshToken) && tokenProvider.refreshTokenValidation(refreshToken)){
                // access token & refresh token 재발급
                Authentication authentication = tokenProvider.getAuthenticationFromToken(refreshToken);
                String newAccessToken = tokenProvider.createToken(authentication, "accessToken");
                tokenProvider.reIssueRefreshToken(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
                log.info("Reissue access token");
            }

        }else {
            log.debug("유효하지 않은 JWT입니다.");
        }
        chain.doFilter(request, response);
    }


    private String extractTokenFromRequest(HttpServletRequest request, String header) {
        String Bearer_token = request.getHeader(header);
        return extractToken(Bearer_token);
    }

    private String extractToken(String bearer_token) {
        if (StringUtils.hasText(bearer_token) && bearer_token.startsWith("Bearer ")) {
            return bearer_token.substring(7);
        }
        return null;
    }
}

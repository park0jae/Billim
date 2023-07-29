package dblab.sharing_flatform.config.security.jwt.provider;

import dblab.sharing_flatform.config.security.details.MemberDetails;
import dblab.sharing_flatform.domain.refresh.RefreshToken;
import dblab.sharing_flatform.exception.auth.ValidateTokenException;
import dblab.sharing_flatform.exception.member.MemberNotFoundException;
import dblab.sharing_flatform.exception.token.TokenNotFoundException;
import dblab.sharing_flatform.repository.refresh.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_ID = "ID";
    private static final String AUTH_USERNAME = "USERNAME";
    public static final String ACCESS = "Access_Token";
    public static final String REFRESH = "Refresh_Token";

    private final long tokenValidityMilliSeconds;
    private final long refreshTokenValidityMilliSeconds;
    private final String originSecretKey;
    private final RefreshTokenRepository refreshTokenRepository;

    private Key secretKey;

    public TokenProvider(@Value("${jwt.token-validity-in-seconds}") long tokenValiditySeconds,
                         @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityMilliSeconds,
                         @Value("${jwt.secret_key}") String originSecretKey, RefreshTokenRepository tokenRepository) {
        this.tokenValidityMilliSeconds =  tokenValiditySeconds * 1000;
        this.refreshTokenValidityMilliSeconds = refreshTokenValidityMilliSeconds;
        this.originSecretKey = originSecretKey;
        this.refreshTokenRepository = tokenRepository;
    }

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Decoders.BASE64.decode(originSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /** 로그인 요청 성공 시 토큰 발급
     *  using in SignService
     *  ( 1. after DB 검증 성공
     *  -> 2. loadByUsernamePassword()로 Authentication 내부에 UserDetails 담아 리턴
     *  -> 3. createAccessToken(authentication)
     *  )
     - authentication -(encode)-> token
     - access Token 생성 (로그인 성공 시 Authentication 발급 -> Authentication 내부 MemberDetails 정보를 바탕으로 Token 생성)
     - MemberDetails implements UserDetails
     **/
    public String createToken(Authentication authentication, String type) {

        MemberDetails principal = (MemberDetails) authentication.getPrincipal();

        String authorities = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date validity = null;
        if(type.equals("accessToken")){
            validity = new Date(now + this.tokenValidityMilliSeconds);
        }else {
            validity = new Date(now + this.refreshTokenValidityMilliSeconds);
        }

        String token = Jwts.builder()
                .addClaims(Map.of(AUTH_ID, principal.getId()))
                .addClaims(Map.of(AUTH_USERNAME, principal.getUsername()))
                .addClaims(Map.of(AUTH_KEY, authorities))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
        return token;
    }

    @Transactional
    public void reIssueRefreshToken(String refreshToken) throws RuntimeException{
        // refresh token을 디비의 그것과 비교해보기
        Authentication authentication = getAuthenticationFromToken(refreshToken);
        RefreshToken findRefreshToken = refreshTokenRepository.findByUsername(authentication.getName()).orElseThrow(TokenNotFoundException::new);

        if(findRefreshToken.getToken().equals(refreshToken)){
            String newRefreshToken = createToken(authentication, "refreshToken");
            refreshTokenRepository.save(findRefreshToken.changeToken(newRefreshToken));
        }
        else {
            log.info("refresh 토큰이 일치하지 않습니다. ");
        }
    }


    /** 권한이 필요한 Request에서 사용
     *  Token -(decode)-> claims 추출
     *  claims로 UsernamePasswordAuthenticationToken 발급
     */
    public Authentication getAuthenticationFromToken(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> authorities = Arrays.asList(claims.get(AUTH_KEY).toString().split(","));

        List<? extends GrantedAuthority> simpleGrantedAuthorities = authorities.stream().map(auth -> new SimpleGrantedAuthority(auth)).collect(Collectors.toList());

        MemberDetails principal = new MemberDetails((String) claims.get(AUTH_ID),(String) claims.get(AUTH_USERNAME), null, simpleGrantedAuthorities, Map.of());

        return new UsernamePasswordAuthenticationToken(principal, token, simpleGrantedAuthorities);
    }


    /** 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new ValidateTokenException();
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new ValidateTokenException();
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new ValidateTokenException();
        }
    }

    public Boolean refreshTokenValidation(String token) {

        // 1차 토큰 검증
        if(!validateToken(token)) return false;

        // DB에 저장한 토큰 비교
        Authentication authentication = getAuthenticationFromToken(token);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(authentication.getName());

        return refreshToken.isPresent() && token.equals(refreshToken.get().getToken());
    }
    // 어세스 토큰 헤더 설정

    @Transactional(readOnly = true)
    public String getRefreshToken(String username) {
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username).orElseThrow(TokenNotFoundException::new);
        return refreshToken.getToken();
    }

}

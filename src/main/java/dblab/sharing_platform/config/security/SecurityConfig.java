package dblab.sharing_platform.config.security;

import dblab.sharing_platform.config.oauth.PrincipalOauth2UserService;
import dblab.sharing_platform.config.security.detailsService.MemberDetailsService;
import dblab.sharing_platform.config.security.handler.JwtAccessDeniedHandler;
import dblab.sharing_platform.config.security.handler.JwtAuthenticationEntryPoint;
import dblab.sharing_platform.config.security.jwt.filter.JwtExceptionFilter;
import dblab.sharing_platform.config.security.jwt.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final MemberDetailsService memberDetailsService;
    private final JwtFilter jwtFilter;
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());

        http
                .httpBasic().disable() // http basic 인증방식 비활성화
                .csrf().disable()  // csrf 관련설정 비활성화
                .formLogin().disable() // Security가 제공하는 로그인 폼 사용 X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션방식 사용 X

                // 시큐리티 예외 처리 핸들러
                .and()
                .exceptionHandling()  // 예외 처리 핸들러 등록
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)// JwtFilter 등록
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class)
                .userDetailsService(memberDetailsService) // 시큐리티 UserDetailsService


                .authorizeRequests() // 권한이 필요한 요청
                .antMatchers("/home", "/auth/sign-up", "/auth/login").permitAll() // 홈, 회원가입, 로그인 요청은 권한 필요X
                .antMatchers("/swagger-uri/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll() // swagger page

                .antMatchers(HttpMethod.GET, "/members").permitAll()
                .antMatchers(HttpMethod.GET, "/members/my-profile").permitAll()
                .antMatchers(HttpMethod.PATCH, "/members/oauth").permitAll()
//                .antMatchers(HttpMethod.GET, "/member/{memberId}").access("@memberGuard.check(#memberId)")

                // 카테고리 생성, 삭제 - ADMIN
                .antMatchers(HttpMethod.DELETE, "/category/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/category/**").hasAuthority("ADMIN")

                // 메시지 조회 - 소유주(수신자, 발신자)
                .antMatchers(HttpMethod.GET, "/messages/{messageId}").access("@sendMessageGuard.check(#messageId) or @receiveMessageGuard.check(#messageId)")

                // 메세지 삭제 - 소유주, ADMIN
                .antMatchers(HttpMethod.DELETE, "/messages/{messageId}/sent").access("@sendMessageGuard.check(#messageId)")
                .antMatchers(HttpMethod.DELETE, "/messages/{messageId}/received").access("@receiveMessageGuard.check(#messageId)")

                // 게시글 생성, 수정 , 삭제 - 소유주, ADMIN
                .antMatchers(HttpMethod.DELETE, "/posts/{postId}").access("@postGuard.check(#postId)")
                .antMatchers(HttpMethod.PATCH, "/posts/{postId}").access("@postGuard.check(#postId)")

                // 댓글 삭제 - 소유주 , ADMIN
                .antMatchers(HttpMethod.DELETE, "/comments/{commentId}").access("@commentGuard.check(#commentId)")

                // 거래 생성, 취소 - 소유주 , ADMIN
                .antMatchers(HttpMethod.POST, "/trades/{postId}").access("@postGuard.check(#postId)")
                .antMatchers(HttpMethod.PATCH, "/trades/trade/{tradeId}").access("@tradeGuard.check(#tradeId)")
                .antMatchers(HttpMethod.DELETE, "/trades/{tradeId}").access("@tradeGuard.check(#tradeId)")

                // 리뷰 조회, 작성, 삭제 - 소유주 , ADMIN
                .antMatchers(HttpMethod.POST, "/reviews/{tradeId}").access("@reviewGuard.check(#tradeId)")
                .antMatchers(HttpMethod.DELETE, "/reviews/{tradeId}").access("@reviewGuard.check(#tradeId)")
                .antMatchers(HttpMethod.GET, "/reviews/all").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/reviews").hasAnyAuthority("ADMIN", "MANAGER", "USER")

                // 신고 조회, 삭제
                .antMatchers(HttpMethod.DELETE, "/reports/{reportId}").access("@reportGuard.check(#reportId)")
                .antMatchers(HttpMethod.GET, "/reports").hasAnyAuthority("ADMIN")


                .and()
                //OAuth 2.0 기반 인증을 처리하기위해 Provider와의 연동을 지원
                .oauth2Login()
                //인증에 성공하면 실행할 handler (redirect 시킬 목적)
                //OAuth 2.0 Provider로부터 사용자 정보를 가져오는 엔드포인트를 지정하는 메서드
                .userInfoEndpoint().userService(principalOauth2UserService);

    // 시큐리티 설정 끝
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean bean
                = new FilterRegistrationBean(new CorsFilter(source));

        bean.setOrder(0);
        return source;
    }

}

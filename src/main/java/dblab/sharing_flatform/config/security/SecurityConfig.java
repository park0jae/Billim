package dblab.sharing_flatform.config.security;

import dblab.sharing_flatform.config.security.detailsService.MemberDetailsService;
import dblab.sharing_flatform.config.security.exceptionAdvisor.JwtAccessDeniedHandler;
import dblab.sharing_flatform.config.security.exceptionAdvisor.JwtAuthenticationEntryPoint;
import dblab.sharing_flatform.config.security.jwt.JwtSecurityConfig;
import dblab.sharing_flatform.config.security.jwt.support.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final TokenProvider tokenProvider;

    private final MemberDetailsService memberDetailsService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 시큐리티 설정 시작

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

                .authorizeRequests() // 권한이 필요한 요청
                .antMatchers("/home", "/sign-up","/login").permitAll() // 홈, 회원가입, 로그인 요청은 권한 필요X

                .antMatchers(HttpMethod.GET, "/adminPage").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/managerPage").hasAuthority("MANAGER")
                .antMatchers(HttpMethod.GET, "/userPage").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "/authenticate").authenticated()

                .and()
                .userDetailsService(memberDetailsService)
                .apply(new JwtSecurityConfig(tokenProvider));

                // 시큐리티 설정 끝

        return http.build();
    }

}

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
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class)
                .userDetailsService(memberDetailsService)

                .authorizeRequests()
                .antMatchers("/home", "/auth/sign-up", "/auth/login").permitAll()
                .antMatchers("/swagger-uri/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()

                .antMatchers(HttpMethod.GET, "/members").permitAll()
                .antMatchers(HttpMethod.GET, "/members/my-profile").permitAll()
                .antMatchers(HttpMethod.PATCH, "/members/oauth").permitAll()

                .antMatchers(HttpMethod.DELETE, "/category/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/category/**").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/messages/received").permitAll()
                .antMatchers(HttpMethod.GET, "/messages/sent").permitAll()
                .antMatchers(HttpMethod.GET, "/messages/{messageId}").access("@sendMessageGuard.check(#messageId) or @receiveMessageGuard.check(#messageId)")
                .antMatchers(HttpMethod.DELETE, "/messages/{messageId}/sent").access("@sendMessageGuard.check(#messageId)")
                .antMatchers(HttpMethod.DELETE, "/messages/{messageId}/received").access("@receiveMessageGuard.check(#messageId)")

                .antMatchers(HttpMethod.DELETE, "/posts/{postId}").access("@postGuard.check(#postId)")
                .antMatchers(HttpMethod.PATCH, "/posts/{postId}").access("@postGuard.check(#postId)")

                .antMatchers(HttpMethod.DELETE, "/comments/{commentId}").access("@commentGuard.check(#commentId)")

                .antMatchers(HttpMethod.POST, "/trades/{postId}").access("@postGuard.check(#postId)")
                .antMatchers(HttpMethod.PATCH, "/trades/trade/{tradeId}").access("@tradeGuard.check(#tradeId)")
                .antMatchers(HttpMethod.DELETE, "/trades/{tradeId}").access("@tradeGuard.check(#tradeId)")

                .antMatchers(HttpMethod.POST, "/reviews/{tradeId}").access("@reviewGuard.check(#tradeId)")
                .antMatchers(HttpMethod.DELETE, "/reviews/{tradeId}").access("@reviewGuard.check(#tradeId)")
                .antMatchers(HttpMethod.GET, "/reviews/all").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/reviews").hasAnyAuthority("ADMIN", "MANAGER", "USER")

                .antMatchers(HttpMethod.DELETE, "/reports/{reportId}").access("@reportGuard.check(#reportId)")
                .antMatchers(HttpMethod.GET, "/reports").hasAnyAuthority("ADMIN")

                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(principalOauth2UserService);

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

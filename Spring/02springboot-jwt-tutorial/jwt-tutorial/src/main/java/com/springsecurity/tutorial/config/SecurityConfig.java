package com.springsecurity.tutorial.config;

import com.springsecurity.tutorial.jwt.JwtAccessDeniedHandler;
import com.springsecurity.tutorial.jwt.JwtAuthenticationEntryPoint;
import com.springsecurity.tutorial.jwt.JwtSecurityConfig;
import com.springsecurity.tutorial.jwt.TokenProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity  //기본적인 Web 보안 활성화
@EnableMethodSecurity   //권한 처리 애노테이션을 메소드 단위로 추가
public class SecurityConfig {

    /**
     * TokenProvider, JwtAuthenticationEntryPoint, JwtAccessDeniedHandler 주입
     * */
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final CorsFilter corsFilter;

    public SecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf(AbstractHttpConfigurer::disable)

                //exception을 핸들링할 때, 만들었던 클래스인 jwtAccessDeniedHandler, jwtAuthenticationEntryPoint 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                //HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/api/hello", "/api/authenticate", "/api/signup").permitAll()  //여기 있는 요청들은 인증 없이 접근 허용
                        //로그인 API, 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 모두 permitAll
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()   //나머지 요청들은 모두 인증돼야 함
                )

                //세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                //h2-console 설정
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                //JwtFilter를 addFilterBefore()로 등록했던 JwtSecurityConfig 클래스도 적용
                .with(new JwtSecurityConfig(tokenProvider), customizer -> {});

        return http.build();

    }

}
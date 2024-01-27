package com.springsecurity.tutorial.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

//JWT를 위한 커스텀 필터
public class JwtFilter extends GenericFilterBean {
    /**
     * GenericFilterBean을 extends해서 doFilter를 Override
     * 실제 필터링 로직은 doFilter 내부에 작성
     * */

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;    //TokenProvider 주입받음

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * 토큰의 인증정보를 현재 실행 중인 SecurityContext에 저장
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);  //httpServletRequest에서 토큰을 받아옴
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            //토큰 유효성 검증 통과(정상 토큰)
            Authentication authentication = tokenProvider.getAuthentication(jwt);   //토큰에서 authentication객체 받아옴
            SecurityContextHolder.getContext().setAuthentication(authentication);   //SecurityContext에 저장(set)
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

        } else {
            //토큰 유효성 검증 통과 X
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 필터링하기 위해선 토큰 정보 필요
     * -> Request Header에서 토큰 정보를 꺼내옴
     * */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}

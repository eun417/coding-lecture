package com.springsecurity.tutorial.controller;

import com.springsecurity.tutorial.dto.LoginDto;
import com.springsecurity.tutorial.dto.TokenDto;
import com.springsecurity.tutorial.jwt.JwtFilter;
import com.springsecurity.tutorial.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    //TokenProvider, AuthenticationManagerBuilder 주입받음
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")   //로그인 API 경로
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        //파라미터로 받은 LoginDto의 username, passoword를 이용해서 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        /**
         * authenticationToken을 이용해서 authenticate 메소드가 실행될 때,
         * CustomUserDetailsService에 있는 loadUserByUsername 메소드 실행
         * -> 결과값으로 Authentication객체 생성
         * */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //생성된 Authentication객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Authentication객체(인증정보)를 기반으로 createToken 메소드를 통해서 JWT Token 생성
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        //JWT Token을 Response Header에 넣어줌
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        //TokenDto를 이용해서 ResponseBody에 넣어서 리턴
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

}

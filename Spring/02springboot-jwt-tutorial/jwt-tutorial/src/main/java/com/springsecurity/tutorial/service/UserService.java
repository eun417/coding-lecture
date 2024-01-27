package com.springsecurity.tutorial.service;

import java.util.Collections;
import com.springsecurity.tutorial.dto.UserDto;
import com.springsecurity.tutorial.entity.Authority;
import com.springsecurity.tutorial.entity.User;
import com.springsecurity.tutorial.exception.DuplicateMemberException;
import com.springsecurity.tutorial.exception.NotFoundMemberException;
import com.springsecurity.tutorial.repository.UserRepository;
import com.springsecurity.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    //UserRepository, PasswordEncoder 주입받음
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입 로직 수행
     * */
    @Transactional
    public UserDto signup(UserDto userDto) {

        //파라미터로 받아온 UserDto의 username이 DB에 존재하는지 확인
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        //DB에 없으면 권한정보 만듦
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER") //ROLE_USER 권한
                .build();

        //만든 권한정보를 넣고, User정보를 생성
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        //UserRepository의 save 메소드를 통해 DB에 저장
        return UserDto.from(userRepository.save(user));
    }

    /**
     * 유저, 권한정보 가져오는 메소드 2개
     * */
    //파라미터로 받은 username을 기준으로 정보를 가져옴
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    //현재 SecurityContext에 저장된 username의 정보만 가져옴
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}

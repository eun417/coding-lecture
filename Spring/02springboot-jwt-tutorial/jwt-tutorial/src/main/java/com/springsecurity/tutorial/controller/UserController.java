package com.springsecurity.tutorial.controller;

import com.springsecurity.tutorial.dto.UserDto;
import com.springsecurity.tutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입
     * */
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')") //USER, ADMIN 권한 모두 허용
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")    //ADMIN 권한만 호출
    public ResponseEntity<UserDto> getUserInfo(@PathVariable(name="username") String username) {
        //UserService에서 만들었던 username 파라미터를 기준으로 유저 정보, 권한 정보 리턴
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}

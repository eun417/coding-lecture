package com.springsecurity.tutorial.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//토큰 정보를 Response할 때 사용
public class TokenDto {

    private String token;
}
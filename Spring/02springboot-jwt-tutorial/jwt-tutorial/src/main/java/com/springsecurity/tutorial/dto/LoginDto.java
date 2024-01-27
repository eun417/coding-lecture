package com.springsecurity.tutorial.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    @Size(min = 3, max = 50)    //@Valid 관련 어노테이션
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}

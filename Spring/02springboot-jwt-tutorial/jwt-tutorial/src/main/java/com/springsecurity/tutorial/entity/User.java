package com.springsecurity.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity //DB 테이블과 1:1 매핑되는 객체
@Table(name = "users") //테이블 명을 user로 지정
//lombok -> Get, Set, Builder, Constructor 관련 코드 자동 생성
//튜토리얼이므로 lombok 부담없이 사용 BUT 실무에선 고려해야 되는 점들 있음
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id //PK
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 증가
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    /*@ManyToMany, @JoinTable
    User객체와 권한객체의 다대다 관계를 일대다, 다대일 관계의 조인 테이블로 정의*/
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

}
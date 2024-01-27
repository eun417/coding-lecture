package com.springsecurity.tutorial.repository;

import com.springsecurity.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//User 엔티티에 매핑
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * JpaRepository를 extends하면 findAll, save 등 메소드 기본적으로 사용 가능
     * */

    /**
     * username을 기준으로 User 정보를 가져올 때, 권한 정보도 같이 가져옴
     * */
    @EntityGraph(attributePaths = "authorities")    //쿼리가 수행될 때, Lazy 조회 X, Eager 조회 o -> authorities도 같이 가져옴
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
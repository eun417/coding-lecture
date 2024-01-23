package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JpaRepository<Member, Long>
 * Long -> id(식별자 pk)의 타입
 * */
public interface SpringDataJpaMemberRepository  extends JpaRepository<Member, Long>, MemberRepository {

    //JPQL select m from Member m where m.name = ?
    @Override
    Optional<Member> findByName(String name);
//    Optional<Member> findByNameAndId(String name, Long id);
}

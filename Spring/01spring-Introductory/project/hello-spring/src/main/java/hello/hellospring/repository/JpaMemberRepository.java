package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    /**
     * JPA는 EntityManager로 모든 게 동작
     * 앞서 JPA 라이브러리를 추가하면 Spring Boot가 EntityManager 자동 생성
     * → 우리는 만들어둔 걸 Injection 하면 됨
     * */
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        //Insert 콜 다 만들어서 DB에 집어넣고, Member에서 setId()까지 수행
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        //파라미터 : 조회할 타입, 식별자 pk값
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    /**
     * pk 기반이 아닌 나머지들은 JPQL 작성해줘야 함
     * */
    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        /**
         * JPQL
         * 객체(Entity)를 대상으로 쿼리를 날림 → SQL로 번역
         * */
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}

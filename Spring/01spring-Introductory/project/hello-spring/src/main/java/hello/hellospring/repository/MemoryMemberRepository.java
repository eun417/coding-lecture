package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {

    //실무에선 동시성 문제가 있을 수 있어서 공유되는 변수일 땐 concurrent HashMap 사용
    //지금은 단순하게 HashMap 사용
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);   //id 세팅
        store.put(member.getId(), member);  //store(Map)에 저장
        return member;  //저장된 결과 반환
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));  //id값이 null이어도 반환 -> 클라이언트에서 사용 가능
    }

    @Override
    public Optional<Member> findByName(String name) {
        //member.getName()이 파라미터로 넘어온 name과 같은지 확인하고, 찾으면 반환
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //store 안에 있는 Member들 반환
    }

    /*데이터 클리어*/
    public void clearStore() {
        store.clear();
    }
}

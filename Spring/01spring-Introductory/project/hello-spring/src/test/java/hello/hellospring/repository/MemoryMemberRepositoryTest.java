package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    /**
     * @AfterEach : 한번에 여러 테스트를 실행하면 메모리 DB에 직전 테스트의 결과가 남을 수 있다.
     * 이렇게 되면 다음 이전 테스트 때문에 다음 테스트가 실패할 가능성이 있다.
     * @AfterEach 를 사용하면 각 테스트가 종료될 때마다 이 기능을 실행한다.
     * 여기서는 메모리 DB에 저장된 데이터를 삭제한다.
     * 테스트는 각각 독립적으로 실행되어야 한다. 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아니다.
     * */
    @AfterEach
    public void afterEach() {
        repository.clearStore();    //테스트 실행되고 끝날 때마다 한 번씩 저장소 비움
    }

    /*findById() 검증*/
    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        //반환 타입이 Optional이면 값을 꺼낼 때 get() ... 좋은 방법은 아니지만 테스트 코드니까 사용
        Member result = repository.findById(member.getId()).get();

        //검증
        //new 해서 만든 Member와 DB에서 꺼낸 Member가 똑같으면 true
        //System.out.println("result = "+ (result == member));
        //Assertions.assertEquals(member, result);  //junit.jupiter
        assertThat(member).isEqualTo(result);    //assertj.core
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}

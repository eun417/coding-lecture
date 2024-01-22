package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    //테스트에선 필드 기반으로 @Autowired 받으면 편함
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("spring");

        //when
        Long saveId = memberService.join(member);

        //then
        //우리가 저장한 것이 repository에 있는지 확인
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("spring");

        //member1과 동일한 name인 member2 생성
        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        /*member1과 name이 똑같기 때문에
        validateDuplicateMember(member); 에서 예외 터져야함

        즉, memberService.join(member2) 로직을 실행하면
        IllegalStateException 예외 터져야 함*/

        //try cath 방법
//        try {
//            memberService.join(member2);
//            fail();
//        } catch (IllegalStateException e) {
//            //정상적으로 동작
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//            /*validateDuplicateMember(Member member) 메소드에 있는
//            IllegalStateException("이미 존재하는 회원입니다."); 메시지 동일해야 함*/
//        }

        //assertThrows 방법
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        //then
    }
}
package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @Controller 있으면 MemberController 객체를 생성해서 Spring에 넣어두고, 관리
 * 스프링 컨테이너에서 스프링 빈이 관리된다.
 * */
@Controller
public class MemberController {

    //private final MemberService memberService = new Member();
    /*Spring이 관리하게 되면 Spring 컨테이너에 등록하고 Spring 컨테이너로부터 받아서 사용하도록 바꿔야함
    new를 사용하면 MemberController 말고 다른 여러 Controller들이 MemberService를 가져다 쓸 수 있음
    BUT 여러 개 생성할 필요 없아 하나만 생성해놓고 사용하면 됨*/

    /**
     * DI(의존성 주입) 3가지 방법
     * 1. 필드 주입
     * 2. setter 주입
     * 3. 생성자 주입
     * */

    //1. 필드 주입
//    @Autowired private MemberService memberService;

    //2. setter 주입
//    @Autowired
//    public void setMemberService(MemberService memberService) {
//        this.memberService = memberService();
//    }

    //3. 생성자 주입
    private final MemberService memberService;

    /**
     * @Autowired
     * Spring이 Spring 컨테이너에 있는 MemberService를 가져다가 연결해줌*/
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        //페이지 이동
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

//        System.out.println("member = " + member.getName());

        memberService.join(member);

        return "redirect:/";    //홈 화면으로 이동
    }

    @GetMapping("/members")
    public String list(Model model) {
        //모든 회원 조회
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }
}

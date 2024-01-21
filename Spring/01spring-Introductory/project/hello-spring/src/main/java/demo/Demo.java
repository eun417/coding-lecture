package demo;

import org.springframework.stereotype.Service;

/**
 * hello.hellospirng을 벗어난 클래스를 컴포넌트 스캔하면?
 * 우리가 실행시키고 있는 것은 HelloSpringApplication
 * helloSpringApplication의 패키지인 hello.hellospring을 포함해서
 * 하위 패키지들을 자동으로 Spring이 전부 찾아서 스프링 빈으로 등록
 * 다른 것들은 기본적으로 컴포넌트 스캔 X ->  스프링 빈 등록 X
 * */
@Service
public class Demo {
}

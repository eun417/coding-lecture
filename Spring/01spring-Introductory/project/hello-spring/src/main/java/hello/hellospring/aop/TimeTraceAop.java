package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP를 Spring Bean에 등록하는 방법
 * 1. SpringConfig에 Bean 등록
 * 2. 컴포넌트 스캔 사용 ... @Component
 * */
@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))")    //hello.hellospring 하위 모두 적용
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());   //어떤 메소드를 콜하는지 이름 보여줌

        try {
//            Object result = joinPoint.proceed();    //다음 메소드로 진행
            return joinPoint.proceed(); //인라인으로 합침
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }

    }
}

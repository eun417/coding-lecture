package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /*/ : localhost 호출하면 첫 화면*/
    @GetMapping("/")
    public String home() {
        return "home";
    }
}

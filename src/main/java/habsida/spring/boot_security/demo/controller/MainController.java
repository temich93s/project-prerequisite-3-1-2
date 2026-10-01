package habsida.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    public MainController() {}

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}

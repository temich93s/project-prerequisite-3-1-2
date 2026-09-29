package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    public UserController() {}

    @GetMapping(value = "/user")
    public String user(ModelMap model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }
}

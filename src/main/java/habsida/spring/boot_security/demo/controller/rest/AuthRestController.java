package habsida.spring.boot_security.demo.controller.rest;

import habsida.spring.boot_security.demo.dto.UserDto;
import habsida.spring.boot_security.demo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        UserDto user = ((User) authentication.getPrincipal())
                .toUserDtoWithoutPassword();

        return Map.of(
                "id", user.getId(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "age", user.getAge(),
                "email", user.getEmail(),
                "roles", user.getRoleDtoNames()
        );
    }
}
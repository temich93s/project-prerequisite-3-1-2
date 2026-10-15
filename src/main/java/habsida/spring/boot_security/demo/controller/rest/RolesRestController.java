package habsida.spring.boot_security.demo.controller.rest;

import habsida.spring.boot_security.demo.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RolesRestController {

    private final RoleService roleService;

    public RolesRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public Set<String> getRoles() {
        return roleService.findAllRoleDtoNames();
    }
}
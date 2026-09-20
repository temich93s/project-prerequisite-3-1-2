package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findRoleByName(String name);
}

package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Optional<Role> findRoleByName(String name);
    Set<String> findAllRolesNames();
}

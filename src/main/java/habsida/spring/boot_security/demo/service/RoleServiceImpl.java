package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Role> findRoleByDtoName(String name) {
        return roleRepository.findByName("ROLE_" + name);
    }

    @Override
    public Set<String> findAllRoleDtoNames() {
        return roleRepository.findAll().stream().map(Role::getRoleDtoName).collect(Collectors.toSet());
    }
}

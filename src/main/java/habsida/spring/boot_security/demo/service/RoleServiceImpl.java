package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.dto.RoleDto;
import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<RoleDto> findRoleByName(String name) {
        return roleRepository.findByName(name).map(Role::toRoleDto);
    }
}

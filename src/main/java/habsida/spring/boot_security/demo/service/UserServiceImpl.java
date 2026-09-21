package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.exception.RoleNotFoundException;
import habsida.spring.boot_security.demo.exception.UserNotFoundException;
import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleService roleService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void addUser(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleService.findRoleByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeUserById(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUser(User user, String roleName) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(user.getId())));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleService.findRoleByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }
}

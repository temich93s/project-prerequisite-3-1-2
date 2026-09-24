package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.dto.UserDto;
import habsida.spring.boot_security.demo.exception.RoleNotFoundException;
import habsida.spring.boot_security.demo.exception.UserNotFoundException;
import habsida.spring.boot_security.demo.model.Role;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(User::toUserDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)))
                .toUserDto();
    }

    @Transactional
    @Override
    public void addUser(UserDto userDto) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoleNames()) {
            roles.add(roleService.findRoleByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException(roleName))
            );
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userDto.toUser(roles));
    }

    @Transactional
    @Override
    public void removeUserById(long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(userDto.getId())));

        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoleNames()) {
            roles.add(roleService.findRoleByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException(roleName))
            );
        }

        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setRoles(roles);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());

        userRepository.save(user);
    }
}

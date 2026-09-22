package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.dto.RoleDto;
import habsida.spring.boot_security.demo.dto.UserDto;
import habsida.spring.boot_security.demo.exception.RoleNotFoundException;
import habsida.spring.boot_security.demo.exception.UserNotFoundException;
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
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(User::toUserDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)))
                .toUserDto();
    }

    @Transactional
    @Override
    public void addUser(UserDto userDto, String roleName) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        RoleDto roleDto = roleService.findRoleByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName))
                .toRole().toRoleDto();
        userDto.setRoles(Set.of(roleDto));
        userRepository.save(userDto.toUser());
    }

    @Transactional
    @Override
    public void removeUserById(long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUser(UserDto userDto, String roleName) {
        getUserById(userDto.getId());
        addUser(userDto, roleName);
    }
}

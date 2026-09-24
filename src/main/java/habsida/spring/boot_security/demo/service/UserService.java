package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto getUserById(long id);
    void addUser(UserDto userDto);
    void removeUserById(long id);
    void updateUser(UserDto userDto);
}

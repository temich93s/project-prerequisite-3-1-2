package habsida.spring.boot_security.demo.service;

import habsida.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    void addUser(User user, String roleName);
    void removeUserById(long id);
    void updateUser(User user, String roleName);
}

package habsida.spring.boot_security.demo.controller.rest;

import habsida.spring.boot_security.demo.dto.UserDto;
import habsida.spring.boot_security.demo.dto.validation.OnCreate;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
    private final UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable long id, @Valid @RequestBody UserDto userDto) {
        userDto.setId(id);
        userService.updateUser(userDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.removeUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addUser")
    public ResponseEntity<Void> addUser(@Validated({Default.class, OnCreate.class}) @RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return ResponseEntity.noContent().build();
    }
}

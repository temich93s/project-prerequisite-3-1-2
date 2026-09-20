package habsida.spring.boot_security.demo.controller;


import habsida.spring.boot_security.demo.exception.UserNotFoundException;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String index(ModelMap model) {
        return "index";
    }

    @GetMapping(value = "/user")
    public String user(ModelMap model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = "/admin/userList")
    public String userList(ModelMap model) {
        try {
            List<User> users = userService.getUsers();
            model.addAttribute("users", users);
            model.addAttribute("loadSuccess", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "Server error, try later");
            model.addAttribute("loadSuccess", false);
        }
        return "userList";
    }

    @GetMapping(value = "/admin/addUser")
    public String addUser(ModelMap model) {
        return "addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(@ModelAttribute User user, @RequestParam String roleName, ModelMap model) {
        try {
            userService.addUser(user, roleName);
            model.addAttribute("message", "User added successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "Server error, try later");
        }
        return "addUser";
    }

    @GetMapping(value = "/admin/removeUser")
    public String removeUser(ModelMap model) {
        return "removeUser";
    }

    @PostMapping(value = "/admin/removeUser")
    public String removeUser(@RequestParam long id, ModelMap model) {
        try {
            userService.removeUserById(id);
            model.addAttribute("message", "User removed successfully");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "User not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "Server error, try later");
        }
        return "removeUser";
    }

    @GetMapping(value = "/admin/updateUser")
    public String updateUser(ModelMap model) {
        return "updateUser";
    }

    @PostMapping(value = "/admin/updateUser")
    public String updateUser(@ModelAttribute User user, ModelMap model) {
        try {
            userService.updateUser(user);
            model.addAttribute("message", "User updated successfully");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "User not found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "Server error, try later");
        }
        return "updateUser";
    }
}

package habsida.spring.boot_security.demo.controller;


import habsida.spring.boot_security.demo.dto.UserDto;
import habsida.spring.boot_security.demo.exception.RoleNotFoundException;
import habsida.spring.boot_security.demo.exception.UserNotFoundException;
import habsida.spring.boot_security.demo.model.User;
import habsida.spring.boot_security.demo.service.RoleService;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/accessDenied")
    public String accessDenied() {
        return "accessDenied";
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
            List<UserDto> userDtoList = userService.getUsers();
            model.addAttribute("users", userDtoList);
            model.addAttribute("loadSuccess", true);
        } catch (Exception e) {
            logger.error("Failed to userList", e);
            model.addAttribute("message", "Server error, try later");
            model.addAttribute("loadSuccess", false);
        }
        return "userList";
    }

    @GetMapping(value = "/admin/addUser")
    public String addUser(ModelMap model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roleNames", roleService.findAllRolesNames());
        return "addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(
            @Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult bindingResult,
            ModelMap model,
            RedirectAttributes redirectAttributes
    ) {
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            bindingResult.rejectValue("password", "password.empty", "Password is required");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("roleNames", roleService.findAllRolesNames());
            return "addUser";
        }
        try {
            userService.addUser(userDto);
            redirectAttributes.addFlashAttribute("message", "User added successfully");
        } catch (RoleNotFoundException e) {
            logger.error("Failed to addUser", e);
            redirectAttributes.addFlashAttribute("message", "Role not found");
        } catch (Exception e) {
            logger.error("Failed to addUser", e);
            redirectAttributes.addFlashAttribute("message", "Server error, try later");
        }
        return "redirect:/admin/userList";
    }

    @PostMapping(value = "/admin/removeUser/{id}")
    public String removeUser(
            @PathVariable long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.removeUserById(id);
            redirectAttributes.addFlashAttribute("message", "User removed successfully");
        } catch (UserNotFoundException e) {
            logger.error("Failed to removeUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "User not found");
        } catch (Exception e) {
            logger.error("Failed to removeUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "Server error, try later");
        }
        return "redirect:/admin/userList";
    }

    @GetMapping(value = "/admin/updateUser/{id}")
    public String updateUser(
            @PathVariable long id,
            ModelMap model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UserDto userDto = userService.getUserById(id);
            model.addAttribute("userDto", userDto);
            model.addAttribute("roleNames", roleService.findAllRolesNames());
            return "updateUser";
        } catch (UserNotFoundException e) {
            logger.error("Failed to updateUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "User not found");
            return "redirect:/admin/userList";
        }
    }

    @PostMapping(value = "/admin/updateUser/{id}")
    public String updateUser(
            @PathVariable long id,
            @Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult bindingResult,
            ModelMap model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roleNames", roleService.findAllRolesNames());
            return "updateUser";
        }
        try {
            userDto.setId(id);
            userService.updateUser(userDto);
            redirectAttributes.addFlashAttribute("message", "User updated successfully");
        } catch (RoleNotFoundException e) {
            logger.error("Failed to updateUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "Role not found");
        } catch (UserNotFoundException e) {
            logger.error("Failed to updateUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "User not found");
        } catch (Exception e) {
            logger.error("Failed to updateUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "Server error, try later");
        }
        return "redirect:/admin/userList";
    }
}

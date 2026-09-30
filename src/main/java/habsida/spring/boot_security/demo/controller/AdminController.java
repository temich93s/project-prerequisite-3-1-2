package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.dto.UserDto;
import habsida.spring.boot_security.demo.exception.RoleNotFoundException;
import habsida.spring.boot_security.demo.exception.UserAlreadyExistsException;
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
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin/userList")
    public String userList(ModelMap model, Authentication authentification) {
        try {
            List<UserDto> userDtoList = userService.getUsers();
            model.addAttribute("users", userDtoList);
            UserDto currentUser = ((User) authentification.getPrincipal()).toUserDtoWithoutPassword();
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("roleDtoNames", roleService.findAllRoleDtoNames());
            model.addAttribute("loadSuccess", true);
        } catch (Exception e) {
            logger.error("Failed to userList", e);
            model.addAttribute("message", "Server error, try later");
            model.addAttribute("loadSuccess", false);
        }
        return "userList";
    }

    @GetMapping(value = "/admin/addUser")
    public String addUser(ModelMap model, Authentication authentification) {
        UserDto user = ((User) authentification.getPrincipal()).toUserDtoWithoutPassword();
        model.addAttribute("currentUser", user);
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roleDtoNames", roleService.findAllRoleDtoNames());
        return "addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(
            @Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult bindingResult,
            ModelMap model,
            RedirectAttributes redirectAttributes,
            Authentication authentification
    ) {
        if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
            bindingResult.rejectValue("password", "password.empty", "Password is required");
        }
        if (bindingResult.hasErrors()) {
            UserDto user = ((User) authentification.getPrincipal()).toUserDtoWithoutPassword();
            model.addAttribute("currentUser", user);
            model.addAttribute("roleDtoNames", roleService.findAllRoleDtoNames());
            return "addUser";
        }
        try {
            userService.addUser(userDto);
            redirectAttributes.addFlashAttribute("message", "User added successfully");
        } catch (RoleNotFoundException e) {
            logger.error("Failed to addUser", e);
            redirectAttributes.addFlashAttribute("message", "Role not found");
        } catch (UserAlreadyExistsException e) {
            logger.error("Failed to addUser", e);
            redirectAttributes.addFlashAttribute("message", "User with same username already exists");
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

    @PostMapping(value = "/admin/updateUser/{id}")
    public String updateUser(
            @PathVariable long id,
            @Valid @ModelAttribute("userDto") UserDto userDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            redirectAttributes.addFlashAttribute("message", errorMessage);
            return "redirect:/admin/userList";
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
        } catch (UserAlreadyExistsException e) {
            logger.error("Failed to updateUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "User with same username already exists");
        } catch (Exception e) {
            logger.error("Failed to updateUser id {}", id, e);
            redirectAttributes.addFlashAttribute("message", "Server error, try later");
        }
        return "redirect:/admin/userList";
    }
}

package habsida.spring.boot_security.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "habsida.spring.boot_security.demo.controller.rest")
public class RestExceptionHandler {

    // Пользователь не найден — 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "User not found"
        );

        problem.setTitle("User Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    // Email уже существует — 409
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExists(UserAlreadyExistsException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "User with this email already exists"
        );

        problem.setTitle("User Already Exists");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    // Роль не найдена — 400
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleRoleNotFound(RoleNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Selected role does not exist"
        );

        problem.setTitle("Role Not Found");

        return ResponseEntity.badRequest().body(problem);
    }

    // Ошибки @Valid — 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }
}

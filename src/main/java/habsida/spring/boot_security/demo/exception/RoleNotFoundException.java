package habsida.spring.boot_security.demo.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String roleName) {
        super("Role: " + roleName + " not found");
    }
}

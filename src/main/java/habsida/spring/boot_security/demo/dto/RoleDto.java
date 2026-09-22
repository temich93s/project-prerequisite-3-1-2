package habsida.spring.boot_security.demo.dto;

import habsida.spring.boot_security.demo.model.Role;

public class RoleDto {
    private Long id;

    private String name;

    // Getter/Setter

    public Long getId() {
            return id;
        }

    public void setId(Long id) {
            this.id = id;
        }

    public String getName() {
            return name;
        }

    public void setName(String name) {
            this.name = name;
        }

    // Mapping

    public Role toRole() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }
}

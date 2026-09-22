package habsida.spring.boot_security.demo.model;

import habsida.spring.boot_security.demo.dto.RoleDto;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Getter/Setter

    @Override
    public String getAuthority() {
        return name;
    }

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

    public RoleDto toRoleDto() {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(id);
        roleDto.setName(name);
        return roleDto;
    }
}

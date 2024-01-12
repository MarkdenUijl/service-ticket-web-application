package nl.helvar.servicetickets.roles;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import nl.helvar.servicetickets.interfaces.Identifyable;

public class RoleCreationDTO implements Identifyable {
    private Long id;
    @NotNull
    @Pattern(regexp = "^ROLE_[A-Z_]+$")
    private String name;

    @Override
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

    public Role fromDto() {
        Role role = new Role();

        role.setName(this.getName());

        return role;
    }

    public static RoleCreationDTO toDto(Role role) {
        RoleCreationDTO roleCreationDTO = new RoleCreationDTO();

        roleCreationDTO.setId(role.getId());
        roleCreationDTO.setName(role.getName());

        return roleCreationDTO;
    }
}

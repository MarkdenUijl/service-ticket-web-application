package nl.helvar.servicetickets.roles;

import jakarta.validation.constraints.NotNull;
import nl.helvar.servicetickets.interfaces.Identifyable;

public class RoleDTO {
    private Long id;
    private String name;

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

    public static RoleDTO toDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());

        return roleDTO;
    }
}

package nl.helvar.servicetickets.roles;

import jakarta.validation.constraints.NotNull;
import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.privileges.PrivilegeDTO;

import java.util.List;

public class RoleDTO implements Identifyable {
    private Long id;
    private String name;
    private List<PrivilegeDTO> privileges;

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

    public List<PrivilegeDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeDTO> privileges) {
        this.privileges = privileges;
    }

    public static RoleDTO toDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());

        if (role.getPrivileges() != null) {
            roleDTO.setPrivileges(role.getPrivileges()
                    .stream()
                    .map(PrivilegeDTO::toDto)
                    .toList());
        }

        return roleDTO;
    }
}

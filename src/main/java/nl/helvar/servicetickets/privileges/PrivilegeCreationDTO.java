package nl.helvar.servicetickets.privileges;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import nl.helvar.servicetickets.interfaces.Identifyable;

public class PrivilegeCreationDTO implements Identifyable {
    private Long id;
    @NotNull
    @Pattern(regexp = "^[A-Z_]+_PRIVILEGE$")
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

    public Privilege fromDto() {
        Privilege privilege = new Privilege();

        privilege.setName(this.getName());

        return privilege;
    }

    public static PrivilegeCreationDTO toDto(Privilege privilege) {
        PrivilegeCreationDTO privilegeCreationDTO = new PrivilegeCreationDTO();

        privilegeCreationDTO.setId(privilege.getId());
        privilegeCreationDTO.setName(privilege.getName());

        return privilegeCreationDTO;
    }
}

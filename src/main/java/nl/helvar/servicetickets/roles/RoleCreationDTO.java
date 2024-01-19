package nl.helvar.servicetickets.roles;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.privileges.Privilege;
import nl.helvar.servicetickets.privileges.PrivilegeRepository;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static nl.helvar.servicetickets.privileges.PrivilegeSpecification.privilegeEquals;

public class RoleCreationDTO {
    private Long id;
    @NotNull
    @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "Name does not follow the format: ROLE_ + role name in all caps.")
    private String name;
    private String[] privileges;

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

    public String[] getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String[] privileges) {
        this.privileges = privileges;
    }

    public Role fromDto(PrivilegeRepository privilegeRepository) {
        Role role = new Role();

        role.setName(this.getName());

        if (this.getPrivileges() != null) {
            Set<Privilege> privilegeSet = new HashSet<>();

            for (String privilegeName : this.getPrivileges()) {
                Specification<Privilege> filter = Specification.where(privilegeEquals(privilegeName));

                Optional<Privilege> optionalPrivilege = privilegeRepository.findOne(filter);

                if(optionalPrivilege.isEmpty()) {
                    throw new RecordNotFoundException("There is no privilege with the name '" + privilegeName + "' in the database.");
                } else {
                    Privilege privilege = optionalPrivilege.get();

                    privilegeSet.add(privilege);
                }
            }

            role.setPrivileges(privilegeSet);
        }

        return role;
    }
}

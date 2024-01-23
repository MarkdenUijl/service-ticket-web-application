package nl.helvar.servicetickets.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.roles.Role;
import nl.helvar.servicetickets.roles.RoleRepository;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static nl.helvar.servicetickets.roles.RoleSpecification.roleNameEquals;

public class UserCreationDTO {
    private Long id;
    private String firstName;
    private String lastName;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;
    private String password;
    @Pattern(regexp = "^(?:\\+31|0|\\+0031|\\0031)?\\s?[1-9](?:\\s?\\d){8}$")
    private String phoneNumber;
    private String[] roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public boolean hasAdminRole() {
        if (roles != null) {
            List<String> rolesList = Arrays.asList(roles);
            return rolesList.contains("ROLE_ADMIN");
        } else {
            return false;
        }
    }

    public User fromDto(RoleRepository roleRepository) {
        User user = new User();

        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setEmail(this.getEmail());
        user.setPhoneNumber(this.getPhoneNumber());
        user.setPassword(this.getPassword());

        if (roles != null && roles.length != 0) {
            Set<Role> userRoles = new HashSet<>();

            for (String roleName : this.roles) {
                Specification<Role> filters = Specification.where(roleNameEquals(roleName));
                Optional<Role> optionalRole = roleRepository.findOne(filters);

                if (optionalRole.isEmpty()) {
                    throw new RecordNotFoundException("Could not find any role with name '" + roleName + "' in the database.");
                } else {
                    userRoles.add(optionalRole.get());
                }
            }

            user.setRoles(userRoles);
        }

        return user;
    }
}

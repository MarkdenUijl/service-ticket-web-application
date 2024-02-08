package nl.helvar.servicetickets.privileges;

import jakarta.persistence.*;
import nl.helvar.servicetickets.roles.Role;

import java.util.Set;

@Entity(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
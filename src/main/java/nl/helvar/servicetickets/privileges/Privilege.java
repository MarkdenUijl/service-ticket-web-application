package nl.helvar.servicetickets.privileges;

import jakarta.persistence.*;
import nl.helvar.servicetickets.roles.Role;

import java.util.List;

@Entity(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
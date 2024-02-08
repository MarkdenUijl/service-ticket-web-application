package nl.helvar.servicetickets.roles;

import jakarta.persistence.*;
import nl.helvar.servicetickets.privileges.Privilege;
import nl.helvar.servicetickets.users.User;

import java.util.Set;

@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }
}

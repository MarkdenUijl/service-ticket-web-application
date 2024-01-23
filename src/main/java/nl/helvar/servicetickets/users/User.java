package nl.helvar.servicetickets.users;

import jakarta.persistence.*;
import nl.helvar.servicetickets.roles.Role;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.ticketresponses.TicketResponse;

import java.util.Set;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    @Column(updatable = false, unique = true)
    private String email;
    private String password;
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "submittedBy", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<ServiceTicket> serviceTickets;

    @OneToMany(mappedBy = "submittedBy", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<TicketResponse> ticketResponses;

    // GETTERS AND SETTERS
    public Long getId() {
        return id;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<ServiceTicket> getServiceTickets() {
        return serviceTickets;
    }

    public void setServiceTickets(Set<ServiceTicket> serviceTickets) {
        this.serviceTickets = serviceTickets;
    }

    public Set<TicketResponse> getTicketResponses() {
        return ticketResponses;
    }

    public void setTicketResponses(Set<TicketResponse> ticketResponses) {
        this.ticketResponses = ticketResponses;
    }

    public boolean hasAdminRole() {
        return roles.stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
    }

    public boolean hasPrivilege(String privilegeName) {
        return roles.stream().anyMatch(role -> role.getPrivileges().stream().anyMatch(privilege -> privilege.getName().equals(privilegeName)));
    }
}

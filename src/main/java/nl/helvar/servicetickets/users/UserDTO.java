package nl.helvar.servicetickets.users;

import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.roles.RoleDTO;
import nl.helvar.servicetickets.servicetickets.ServiceTicketDTO;
import nl.helvar.servicetickets.ticketresponses.TicketResponse;

import java.util.List;

public class UserDTO implements Identifyable {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<RoleDTO> roles;
    private List<ServiceTicketDTO> tickets;
    private List<TicketResponse> ticketResponses;

    @Override
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public List<ServiceTicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<ServiceTicketDTO> tickets) {
        this.tickets = tickets;
    }

    public List<TicketResponse> getTicketResponses() {
        return ticketResponses;
    }

    public void setTicketResponses(List<TicketResponse> ticketResponses) {
        this.ticketResponses = ticketResponses;
    }

    public static UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());

        if (user.getRoles() != null) {
            userDTO.setRoles(user.getRoles()
                    .stream()
                    .map(RoleDTO::toDto)
                    .toList());
        }

        if (user.getServiceTickets() != null) {
            userDTO.setTickets(user.getServiceTickets()
                    .stream()
                    .map(ServiceTicketDTO::toDto)
                    .toList());
        }

        return userDTO;
    }

    public static UserDTO toSimpleDto(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        return userDTO;
    }
}

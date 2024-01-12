package nl.helvar.servicetickets.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import nl.helvar.servicetickets.interfaces.Identifyable;

public class UserCreationDTO implements Identifyable {
    private Long id;
    private String firstName;
    private String lastName;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @Pattern(regexp = "^(\\+[0-9]{1,4})?[-.\\s]?\\(?[0-9]+\\)?[-.\\s]?[0-9]+[-.\\s]?[0-9]+$")
    private String phoneNumber;

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

    public User fromDto() {
        User user = new User();

        user.setFirstName(this.getFirstName());
        user.setLastName(this.getLastName());
        user.setEmail(this.getEmail());
        user.setPhoneNumber(this.getPhoneNumber());
        user.setPassword(this.getPassword());

        return user;
    }

    public static UserCreationDTO toDto(User user) {
        UserCreationDTO userCreationDTO = new UserCreationDTO();

        userCreationDTO.setId(user.getId());
        userCreationDTO.setFirstName(user.getFirstName());
        userCreationDTO.setLastName(user.getLastName());
        userCreationDTO.setEmail(user.getEmail());
        userCreationDTO.setPhoneNumber(user.getPhoneNumber());
        userCreationDTO.setPassword(user.getPassword());

        return userCreationDTO;
    }
}

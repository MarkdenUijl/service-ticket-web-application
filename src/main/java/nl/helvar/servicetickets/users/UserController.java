package nl.helvar.servicetickets.users;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.email.EmailService;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;
import static nl.helvar.servicetickets.helpers.UriCreator.createUri;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    public UserController (UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(required = false) String roleName) {
        List<UserDTO> userDTOS = userService.getAllUsers(roleName);

        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable("id") Long id) {
        UserDTO userDTO = userService.findUserById(id);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreationDTO user, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else if (user.getEmail() == null || user.getPassword() == null){
            throw new BadObjectCreationException("User does not contain email or password.");
        } else {
            UserDTO userOutput = userService.createUser(user);

            try {
                String recipient;

                if (user.getFirstName() != null) {
                    recipient = user.getFirstName() + " " + user.getLastName();
                } else {
                    recipient = user.getEmail();
                }

                emailService.sendUserCreationConfirmation("markdenuyl@gmail.com", recipient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            URI uri = createUri(userOutput);

            return ResponseEntity.created(uri).body(userOutput);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> adjustUser(@PathVariable("id")Long id, @Valid @RequestBody UserCreationDTO user, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            UserDTO userOutput = userService.adjustUser(id, user);

            return new ResponseEntity<>(userOutput, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}

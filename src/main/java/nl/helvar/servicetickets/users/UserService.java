package nl.helvar.servicetickets.users;

import nl.helvar.servicetickets.exceptions.EmailExistsException;
import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.roles.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.users.UserSpecification.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public UserDTO createUser(UserCreationDTO userCreationDTO) {
        Specification<User> emailFilter = Specification.where(emailEquals(userCreationDTO.getEmail()));
        Optional<User> existingUser = userRepository.findOne(emailFilter);

        if (existingUser.isPresent()) {
            throw new EmailExistsException("There is already a user registered at that email address.");
        } else {
            String encodedPassword = encoder.encode(userCreationDTO.getPassword());
            userCreationDTO.setPassword(encodedPassword);

            User user = userCreationDTO.fromDto(roleRepository);

            userRepository.save(user);

            return UserDTO.toDto(user);
        }
    }

    public List<UserDTO> getAllUsers(String roleName) {
        Specification<User> filters = Specification.where(StringUtils.isBlank(roleName) ? null : roleLike(roleName));

        List<UserDTO> filteredUsers = userRepository.findAll(filters)
                .stream()
                .map(UserDTO::toDto)
                .toList();

        if (filteredUsers.isEmpty()) {
            throw new RecordNotFoundException("There were no users with those parameters in the database.");
        } else {
            return filteredUsers;
        }
    }

    public UserDTO findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Could not find any user with id '" + id + "' in the database.");
        } else {
            return UserDTO.toDto(optionalUser.get());
        }
    }

    public UserDTO adjustUser(Long id, UserCreationDTO newUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Could not find any user with id '" + id + "' in the database.");
        } else {
            User existingUser = optionalUser.get();

            if (existingUser.hasAdminRole() && newUser.getRoles() != null && !newUser.hasAdminRole()) {
                Specification<User> filters = Specification.where(roleEquals("ROLE_ADMIN"));

                List<User> optionalAdminUsers = userRepository.findAll(filters);

                if (optionalAdminUsers.size() <= 1) {
                    throw new InvalidRequestException("There always needs to be one admin in the system.");
                }
            }

            ObjectCopyUtils.copyNonNullProperties(newUser.fromDto(roleRepository), existingUser);
            userRepository.save(existingUser);

            return UserDTO.toDto(existingUser);
        }
    }

    public String deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Could not find any user with id '" + id + "' in the database.");
        } else {
            User user = optionalUser.get();

            if (user.hasAdminRole()) {
                Specification<User> filters = Specification.where(roleEquals("ROLE_ADMIN"));

                List<User> optionalAdminUsers = userRepository.findAll(filters);

                if (optionalAdminUsers.size() <= 1) {
                    throw new InvalidRequestException("There always needs to be one admin in the system.");
                }
            }

            userRepository.delete(user);

            return "the user '" + user.getFirstName() + " " + user.getLastName() + "' was deleted from the database.";
        }
    }
}

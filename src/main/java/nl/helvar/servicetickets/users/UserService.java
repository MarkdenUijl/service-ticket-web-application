package nl.helvar.servicetickets.users;

import nl.helvar.servicetickets.exceptions.EmailExistsException;
import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.roles.Role;
import nl.helvar.servicetickets.roles.RoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.UserDetailsValidator.hasPrivilege;
import static nl.helvar.servicetickets.roles.RoleSpecification.roleNameEquals;
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
        try {
            String encodedPassword = encoder.encode(userCreationDTO.getPassword());
            userCreationDTO.setPassword(encodedPassword);

            Specification<Role> filter = Specification.where(roleNameEquals("ROLE_USER"));
            Optional<Role> optionalRole = roleRepository.findOne(filter);

            if (optionalRole.isEmpty()) {
                throw new RecordNotFoundException("Could not find any role with name 'ROLE_USER' in the database.");
            } else {
                userCreationDTO.setRoles(new String[]{optionalRole.get().getName()});
            }

            User user = userCreationDTO.fromDto(roleRepository);

            userRepository.save(user);

            return UserDTO.toDto(user);
        } catch (DataIntegrityViolationException e) {
            throw new EmailExistsException("There is already a user registered at that email address.");
        }
    }

    public List<UserDTO> getAllUsers(String roleName) {
        Specification<User> filters = Specification.where(StringUtils.isBlank(roleName) ? null : userRoleLike(roleName));

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

    public UserDTO findUserById(UserDetails userDetails, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Could not find any user with id '" + id + "' in the database.");
        } else {
            if (hasPrivilege("CAN_ACCESS_USERS_PRIVILEGE", userDetails) || userDetails.getUsername().equals(optionalUser.get().getEmail())) {
                return UserDTO.toDto(optionalUser.get());
            } else {
                return UserDTO.toSimpleDto(optionalUser.get());
            }
        }
    }

    public UserDTO adjustUser(UserDetails userDetails ,Long id, UserCreationDTO newUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Could not find any user with id '" + id + "' in the database.");
        } else {
            boolean canModify = hasPrivilege("CAN_MODIFY_USERS_PRIVILEGE", userDetails);

            if (canModify || userDetails.getUsername().equals(optionalUser.get().getEmail())) {
                User existingUser = optionalUser.get();

                if (newUser.getEmail() != null && !existingUser.getEmail().equals(newUser.getEmail())) {
                    throw new InvalidRequestException("Changing an email address is not allowed.");
                }


                if (canModify && newUser.getRoles() != null) {
                    if (existingUser.hasAdminRole() && newUser.getRoles() != null && !newUser.hasAdminRole()) {
                        Specification<User> filters = Specification.where(userRoleEquals("ROLE_ADMIN"));

                        List<User> optionalAdminUsers = userRepository.findAll(filters);

                        if (optionalAdminUsers.size() <= 1) {
                            throw new InvalidRequestException("There always needs to be one admin in the system.");
                        }
                    }
                } else if (!canModify && newUser.getRoles() != null) {
                    throw new InvalidRequestException("You are not allowed to modify roles.");
                }

                ObjectCopyUtils.copyNonNullProperties(newUser.fromDto(roleRepository), existingUser);
                userRepository.save(existingUser);

                return UserDTO.toDto(existingUser);
            } else {
                throw new InvalidRequestException("You are not allowed to modify this user.");
            }
        }
    }

    public String deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RecordNotFoundException("Could not find any user with id '" + id + "' in the database.");
        } else {
            User user = optionalUser.get();

            if (user.hasAdminRole()) {
                Specification<User> filters = Specification.where(userRoleEquals("ROLE_ADMIN"));

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

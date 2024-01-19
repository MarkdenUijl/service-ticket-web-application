package nl.helvar.servicetickets.roles;

import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.privileges.PrivilegeRepository;
import nl.helvar.servicetickets.users.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    public RoleService(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    public RoleDTO createRole(RoleCreationDTO roleCreationDTO) {
        Role role = roleCreationDTO.fromDto(privilegeRepository);

        roleRepository.save(role);

        return RoleDTO.toDto(role);
    }

    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> filteredRoles = roleRepository.findAll()
                .stream()
                .map(RoleDTO::toDto)
                .toList();

        if (filteredRoles.isEmpty()) {
            throw new RecordNotFoundException("There were no roles with those parameters in the database.");
        } else {
            return filteredRoles;
        }
    }

    public RoleDTO findRoleById(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);

        if (optionalRole.isEmpty()) {
            throw new RecordNotFoundException("Could not find any role with id '" + id + "' in the database.");
        } else {
            return RoleDTO.toDto(optionalRole.get());
        }
    }

    public RoleDTO adjustRole(Long id, RoleCreationDTO roleCreationDTO) {
        Optional<Role> optionalRole = roleRepository.findById(id);

        if (optionalRole.isEmpty()) {
            throw new RecordNotFoundException("Could not find any role with id '" + id + "' in the database.");
        } else {
            Role role = optionalRole.get();
            String name = role.getName();

            if (Objects.equals(name, "ROLE_ADMIN") || Objects.equals(name, "ROLE_ENGINEER") || Objects.equals(name, "ROLE_USER")) {
                throw new InvalidRequestException("It is not allowed to change any of the main roles from the system.");
            } else {
                ObjectCopyUtils.copyNonNullProperties(roleCreationDTO.fromDto(privilegeRepository), role);

                roleRepository.save(role);

                return RoleDTO.toDto(role);
            }
        }
    }

    public String deleteRole(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);

        if (optionalRole.isEmpty()) {
            throw new RecordNotFoundException("Could not find any role with id '" + id + "' in the database.");
        } else {
            Role role = optionalRole.get();
            String name = role.getName();

            if (Objects.equals(name, "ROLE_ADMIN") || Objects.equals(name, "ROLE_ENGINEER") || Objects.equals(name, "ROLE_USER")) {
                throw new InvalidRequestException("It is not allowed to delete any of the main roles from the system.");
            } else {
                for (User user : role.getUsers()) {
                    user.removeRole(role);
                }

                role.setUsers(null);

                roleRepository.delete(role);

                return "the role with id '" + id + "' was deleted from the database.";
            }
        }
    }
}

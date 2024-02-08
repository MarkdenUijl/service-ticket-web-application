package nl.helvar.servicetickets.privileges;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    public List<PrivilegeDTO> getAllPrivileges() {
        List<PrivilegeDTO> filteredPrivileges = privilegeRepository.findAll()
                .stream()
                .map(PrivilegeDTO::toDto)
                .toList();

        if (filteredPrivileges.isEmpty()) {
            throw new RecordNotFoundException("There were no privileges with those parameters in the database.");
        } else {
            return filteredPrivileges;
        }
    }

    public PrivilegeDTO findPrivilegeById(Long id) {
        Optional<Privilege> optionalPrivilege = privilegeRepository.findById(id);

        if (optionalPrivilege.isEmpty()) {
            throw new RecordNotFoundException("Could not find any privilege with id '" + id + "' in the database.");
        } else {
            return PrivilegeDTO.toDto(optionalPrivilege.get());
        }
    }
}

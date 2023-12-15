package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.projects.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.servicecontracts.ServiceContractSpecification.*;

@Service
public class ServiceContractService {
    private final ServiceContractRepository serviceContractRepository;
    private final ProjectRepository projectRepository;

    public ServiceContractService(ServiceContractRepository serviceContractRepository, ProjectRepository projectRepository) {
        this.serviceContractRepository = serviceContractRepository;
        this.projectRepository = projectRepository;
    }

    public ServiceContractCreationDTO createServiceContract(ServiceContractCreationDTO serviceContractCreationDTO) {
        Optional<Project> projectOptional = projectRepository.findById(serviceContractCreationDTO.getProjectId());

        if (projectOptional.isEmpty()) {
            throw new RecordNotFoundException("Project with id " + serviceContractCreationDTO.getProjectId() + " was not found.");
        } else {
            ServiceContract serviceContract = serviceContractCreationDTO.fromDto();
            Project project = projectOptional.get();

            project.setServiceContract(serviceContract);

            serviceContractRepository.save(serviceContract);
            projectRepository.save(project);

            serviceContractCreationDTO.setId(serviceContract.getId());
            return serviceContractCreationDTO;
        }
    }

    public List<ServiceContractDTO> getAllServiceContracts(String type) {
        Specification<ServiceContract> filters = null;
        if (!StringUtils.isBlank(type)) {
            filters = typeEquals(type);
        }

        List<ServiceContractDTO> serviceContracts = serviceContractRepository.findAll(filters)
                .stream()
                .map(ServiceContractDTO::toDto)
                .toList();

        if (serviceContracts.isEmpty()) {
            throw new RecordNotFoundException("Could not find any contracts in database.");
        } else {
            return serviceContracts;
        }
    }

    public ServiceContractDTO deleteServiceContract(Long id) {
        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);

        if (serviceContract.isEmpty()) {
            throw new RecordNotFoundException("Could not find any contract with id '" + id + "' in database.");
        } else {
            ServiceContract existingServiceContract = serviceContract.get();
            // Project project = projectRepository.findByServiceContract(existingServiceContract);

            serviceContractRepository.deleteById(id);

            return ServiceContractDTO.toDto(existingServiceContract);
        }
    }
}

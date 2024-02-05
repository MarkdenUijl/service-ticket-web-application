package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.projects.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
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

    public ServiceContractDTO createServiceContract(ServiceContractCreationDTO serviceContractCreationDTO) {
        Optional<Project> projectOptional = projectRepository.findById(serviceContractCreationDTO.getProjectId());

        if (projectOptional.isEmpty()) {
            throw new RecordNotFoundException("Project with id " + serviceContractCreationDTO.getProjectId() + " was not found.");
        } else {
            ServiceContract serviceContract = serviceContractCreationDTO.fromDto();
            Project project = projectOptional.get();

            if (project.getServiceContract() != null) {
                throw new InvalidRequestException("Project with id " + serviceContractCreationDTO.getProjectId() + " already has a contract.");
            }

            project.setServiceContract(serviceContract);

            serviceContractRepository.save(serviceContract);
            projectRepository.save(project);

            return ServiceContractDTO.toDto(serviceContract);
        }
    }

    public List<ServiceContractDTO> getAllServiceContracts(String type) {
        Specification<ServiceContract> filters = null;
        if (!StringUtils.isBlank(type)) {
            filters = contractTypeEquals(type);
        }

        List<ServiceContractDTO> serviceContracts = serviceContractRepository.findAll(filters)
                .stream()
                .map(ServiceContractDTO::toDto)
                .toList();

        if (serviceContracts.isEmpty()) {
            throw new RecordNotFoundException("Could not find any contracts with these parameters in the database.");
        } else {
            return serviceContracts;
        }
    }

    public ServiceContractDTO findById(Long id) {
        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);

        if (serviceContract.isEmpty()) {
            throw new RecordNotFoundException("Could not find any contract with id '" + id + "' in database.");
        } else {
            return ServiceContractDTO.toDto(serviceContract.get());
        }
    }

    public ServiceContractDTO replaceServiceContract(Long id, ServiceContractCreationDTO newServiceContract) {
        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);

        if (serviceContract.isEmpty()) {
            throw new RecordNotFoundException("Could not find any contract with id '" + id + "' in database.");
        } else {
            ServiceContract existingServiceContract = serviceContract.get();

            ObjectCopyUtils.copyNonNullProperties(newServiceContract.fromDto(), existingServiceContract);

            serviceContractRepository.save(existingServiceContract);

            return ServiceContractDTO.toDto(existingServiceContract);
        }
    }

    public String deleteServiceContract(Long id) {
        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);

        if (serviceContract.isEmpty()) {
            throw new RecordNotFoundException("Could not find any contract with id '" + id + "' in database.");
        } else {
            ServiceContract existingServiceContract = serviceContract.get();
            Project project = existingServiceContract.getProject();

            project.setServiceContract(null);

            serviceContractRepository.deleteById(id);
            projectRepository.save(project);

            return "Contract with id " + id + " was successfully deleted.";
        }
    }
}

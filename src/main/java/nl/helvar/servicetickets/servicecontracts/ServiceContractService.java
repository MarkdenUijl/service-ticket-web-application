package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.projects.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                throw new InvalidRequestException(
                    "Project with id " + serviceContractCreationDTO.getProjectId() + " already has a contract."
                );
            }

            project.addHistoricContract(serviceContract);
            project.setServiceContract(serviceContract);

            projectRepository.save(project);

            return ServiceContractDTO.toDto(serviceContract);
        }
    }

    public ServiceContractDTO renewServiceContract(Long id, ServiceContractRenewDTO renewDTO) {
        ServiceContract oldContract = serviceContractRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Could not find any contract with id '" + id + "' in database."
                ));

        Project project = oldContract.getProject();
        if (project == null) {
            throw new InvalidRequestException(
                    "Contract with id " + id + " is not linked to a project and cannot be renewed."
            );
        }

        if (renewDTO.getHours() == null || renewDTO.getHours() <= 0) {
            throw new InvalidRequestException("Renewal requires a positive 'hours' value.");
        }

        LocalDate newStartDate = oldContract.getEndDate().isBefore(LocalDate.now())
                ? LocalDate.now()
                : oldContract.getEndDate();
        LocalDate newEndDate = newStartDate.plusYears(1);

        int newContractTimeMinutes = renewDTO.getHours() * 60;

        ServiceContract newContract = new ServiceContract();
        newContract.setType(renewDTO.getType());
        newContract.setContractTime(newContractTimeMinutes);
        newContract.setUsedTime(0);
        newContract.setStartDate(newStartDate);
        newContract.setEndDate(newEndDate);

        // History + linkage
        newContract.setProject(project);
        newContract.setPreviousContract(oldContract);

        // Add to project history and mark as current
        project.addHistoricContract(newContract);
        project.setServiceContract(newContract);

        // Persist (cascade from project will handle newContract)
        projectRepository.save(project);

        return ServiceContractDTO.toDto(newContract);
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

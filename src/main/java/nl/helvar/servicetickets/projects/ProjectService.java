package nl.helvar.servicetickets.projects;

import nl.helvar.servicetickets.exceptions.DuplicateInDatabaseException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.servicecontracts.ServiceContractRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.UserDetailsValidator.hasPrivilege;
import static nl.helvar.servicetickets.projects.ProjectSpecification.*;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ServiceContractRepository serviceContractRepository;

    public ProjectService(ProjectRepository projectRepository, ServiceContractRepository serviceContractRepository) {
        this.projectRepository = projectRepository;
        this.serviceContractRepository = serviceContractRepository;
    }

    public ProjectDTO createProject(ProjectCreationDTO projectCreationDto) {
        Specification<Project> projectByAddressFilter = Specification.where(findProjectByAddress(
                projectCreationDto.getCity(),
                projectCreationDto.getStreet(),
                projectCreationDto.getZipCode(),
                projectCreationDto.getHouseNumber()
        ));

        Optional<Project> optionalProject = projectRepository.findOne(projectByAddressFilter);

        if (optionalProject.isEmpty()) {
            Project project = projectCreationDto.fromDto(serviceContractRepository);

            projectRepository.save(project);

            return ProjectDTO.toDto(project);
        } else {
            throw new DuplicateInDatabaseException("There was already a project registered at this address.");
        }
    }

    public List<ProjectDTO> getAllProjects(String name,
                                           String city,
                                           String zipCode,
                                           String street,
                                           Integer houseNumber,
                                           Boolean hasServiceContract
    ) {
        Specification<Project> filters = Specification.where(StringUtils.isBlank(name) ? null : projectNameLike(name))
                .and(StringUtils.isBlank(city) ? null : projectCityLike(city))
                .and(StringUtils.isBlank(zipCode) ? null : projectZipCodeLike(zipCode))
                .and(StringUtils.isBlank(street) ? null : projectStreetLike(street))
                .and(houseNumber == null ? null : projectHouseNumberLike(houseNumber))
                .and(hasServiceContract == null ? null : projectServiceContractLike(hasServiceContract));

        List<ProjectDTO> filteredProjects = projectRepository.findAll(filters)
                .stream()
                .map(ProjectDTO::toDto)
                .toList();

        if (filteredProjects.isEmpty()) {
            throw new RecordNotFoundException("There were no projects found with those parameters");
        } else {
            return filteredProjects;
        }
    }

    public ProjectDTO findProjectById(UserDetails userDetails, Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if(project.isEmpty()) {
            throw new RecordNotFoundException("No project found with id " + id);
        } else {
            if (hasPrivilege("CAN_SEE_PROJECTS_PRIVILEGE", userDetails)) {
                return ProjectDTO.toDto(project.get());
            } else {
                return ProjectDTO.toSimpleDto(project.get());
            }
        }
    }


    public ProjectDTO replaceProject(Long id, ProjectCreationDTO newProject) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new RecordNotFoundException("Could not find any project with id '" + id + "' in database.");
        } else {
            Project existingProject = project.get();

            ObjectCopyUtils.copyNonNullProperties(newProject.fromDto(serviceContractRepository), existingProject);

            projectRepository.save(existingProject);

            return ProjectDTO.toDto(existingProject);
        }
    }

    public String deleteProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new RecordNotFoundException("Could not find any project with id '" + id + "' in database.");
        } else {
            projectRepository.delete(project.get());

            return "Project with id '" + id + "' was successfully deleted.";
        }
    }
}
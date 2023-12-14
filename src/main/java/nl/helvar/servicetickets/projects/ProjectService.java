package nl.helvar.servicetickets.projects;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicecontracts.ServiceContractRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.projects.ProjectSpecification.*;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ServiceContractRepository serviceContractRepository;

    public ProjectService(ProjectRepository projectRepository, ServiceContractRepository serviceContractRepository) {
        this.projectRepository = projectRepository;
        this.serviceContractRepository = serviceContractRepository;
    }

    public ProjectCreationDTO createProject(ProjectCreationDTO projectCreationDto) {
        Project project = toProject(projectCreationDto);

        projectRepository.save(project);

        projectCreationDto.setId(project.getId());
        return projectCreationDto;
    }

    public List<ProjectDTO> getAllProjects(String name, String city, String zipCode, String street, Integer houseNumber, Boolean hasServiceContract) {
        Specification<Project> filters = Specification.where(StringUtils.isBlank(name) ? null : nameLike(name))
                .and(StringUtils.isBlank(city) ? null : cityLike(city))
                .and(StringUtils.isBlank(zipCode) ? null : zipCodeLike(zipCode))
                .and(StringUtils.isBlank(street) ? null : streetLike(street))
                .and(houseNumber == null ? null : houseNumberLike(houseNumber))
                .and(hasServiceContract == null ? null : serviceContractLike(hasServiceContract));

        List<ProjectDTO> filteredProjects = projectRepository.findAll(filters)
                .stream()
                .map(this::fromProject)
                .toList();

        if (filteredProjects.isEmpty()) {
            throw new RecordNotFoundException("There were no projects found with those parameters");
        } else {
            return filteredProjects;
        }
    }

    public ProjectDTO findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if(project.isEmpty()) {
            throw new RecordNotFoundException("No project found with id " + id);
        } else {
            return fromProject(project.get());
        }
    }

    public ProjectDTO replaceProject(Long id, ProjectCreationDTO newProject) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new RecordNotFoundException("Could not find any project with id '" + id + "' in database.");
        } else {
            Project existingProject = project.get();

            BeanUtils.copyProperties(newProject, existingProject, "id");

            projectRepository.save(existingProject);

            return fromProject(existingProject);
        }
    }

    public ProjectDTO deleteProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new RecordNotFoundException("Could not find any project with id '" + id + "' in database.");
        } else {
            Project existingProject = project.get();

            projectRepository.delete(existingProject);

            return fromProject(existingProject);
        }
    }

    // MAPPERS:
    public ProjectDTO fromProject(Project project) {
        ProjectDTO projectDto = new ProjectDTO();

        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setCity(project.getCity());
        projectDto.setZipCode(project.getZipCode());
        projectDto.setStreet(project.getStreet());
        projectDto.setHouseNumber(project.getHouseNumber());

        return projectDto;
    }

    public Project toProject(ProjectCreationDTO projectcreationDto) {
        Project project = new Project();

        project.setName(projectcreationDto.getName());
        project.setCity(projectcreationDto.getCity());
        project.setZipCode(projectcreationDto.getZipCode());
        project.setStreet(projectcreationDto.getStreet());
        project.setHouseNumber(projectcreationDto.getHouseNumber());

        if (projectcreationDto.getServiceContractId() != null) {
            Optional<ServiceContract> serviceContract = serviceContractRepository.findById(projectcreationDto.getServiceContractId());

            serviceContract.ifPresent(project::setServiceContract);
        }

        return project;
    }
}
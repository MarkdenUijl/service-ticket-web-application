package nl.helvar.servicetickets.projects;

import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicecontracts.ServiceContractRepository;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.projects.ProjectSpecification.addressLike;
import static nl.helvar.servicetickets.projects.ProjectSpecification.nameLike;

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

    public List<ProjectDTO> getAllProjects(String name, String address) {
        Specification<Project> filters = Specification.where(StringUtils.isBlank(name) ? null : nameLike(name))
                .and(StringUtils.isBlank(address) ? null : addressLike(address));

        return projectRepository.findAll(filters).stream().map(this::fromProject).toList();
    }

    public ProjectDTO findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if(project.isEmpty()) {
            return null;
        } else {
            return fromProject(project.get());
        }
    }

    // MAPPERS:
    public ProjectDTO fromProject(Project project) {
        ProjectDTO projectDto = new ProjectDTO();

        projectDto.id = project.getId();
        projectDto.name = project.getName();
        projectDto.address = project.getAddress();

        return projectDto;
    }

    public Project toProject(ProjectCreationDTO projectcreationDto) {
        Project project = new Project();

        project.setName(projectcreationDto.getName());
        project.setAddress(projectcreationDto.getAddress());

        if (projectcreationDto.getServiceContractId() != null) {
            Optional<ServiceContract> serviceContract = serviceContractRepository.findById(projectcreationDto.getServiceContractId());

            serviceContract.ifPresent(project::setServiceContract);
        }

        return project;
    }
}

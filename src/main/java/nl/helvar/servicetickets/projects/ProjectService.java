package nl.helvar.servicetickets.projects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.projects.ProjectSpecification.addressLike;
import static nl.helvar.servicetickets.projects.ProjectSpecification.nameLike;

@Service
public class ProjectService {
    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = projectDtoToProject(projectDto);

        repository.save(project);

        projectDto.id = project.getId();

        return projectDto;
    }

    public List<ProjectDto> getAllProjects(String name, String address) {
        Specification<Project> filters = Specification.where(StringUtils.isBlank(name) ? null : nameLike(name))
                .and(StringUtils.isBlank(address) ? null : addressLike(address));

        return repository.findAll(filters).stream().map(this::projectToProjectDto).toList();
    }

    public ProjectDto findById(Long id) {
        Optional<Project> project = repository.findById(id);

        if(project.isEmpty()) {
            return null;
        } else {
            return projectToProjectDto(project.get());
        }
    }

    // MAPPERS:
    public ProjectDto projectToProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();

        projectDto.id = project.getId();
        projectDto.name = project.getName();
        projectDto.address = project.getAddress();

        return projectDto;
    }

    public Project projectDtoToProject(ProjectDto projectDto) {
        Project project = new Project();

        project.setName(projectDto.name);
        project.setAddress(projectDto.address);

        return project;
    }
}

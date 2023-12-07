package nl.helvar.servicetickets.projects;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects (
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address
    ) {
        List<ProjectDto> projectDtos = service.getAllProjects(name, address);

        return new ResponseEntity<>(projectDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> findProjectById(@PathVariable("id") Long id) {
        ProjectDto projectDto = service.findById(id);

        if (projectDto == null) {
            throw new RecordNotFoundException("Could not find any projects with id " + id + " in the database.");
        } else {
            return new ResponseEntity<>(projectDto, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createProject(@Valid @RequestBody ProjectDto project, BindingResult br) {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();

            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField());
                sb.append(" : ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }

            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            project = service.createProject(project);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + project.id)
                            .toUriString()
            );

            return ResponseEntity.created(uri).body(project);
        }
    }

//    @PostMapping("/{id}/tickets")
//    public ResponseEntity<ServiceTicket> addServiceTicketToProject(@PathVariable("id") Long id, @RequestBody ServiceTicket ticket) {
//        Optional<Project> project = projectRepository.findById(id);
//
//        if (project.isEmpty()) {
//            throw new RecordNotFoundException("Could not find any project with id '" + id + "' in database.");
//        } else {
//            Project existingProject = project.get();
//
//            existingProject.addTicket(ticket);
//            projectRepository.save(existingProject);
//
//            return new ResponseEntity<>(ticket, HttpStatus.OK);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Project> replaceProject(@PathVariable("id") Long id, @RequestBody Project newProject) {
//        Optional<Project> project = projectRepository.findById(id);
//
//        if (project.isEmpty()) {
//            throw new RecordNotFoundException("Could not find any project with id '" + id + "' in database.");
//        } else {
//            Project existingProject = project.get();
//
//            BeanUtils.copyProperties(newProject, existingProject, "id");
//
//            projectRepository.save(existingProject);
//
//            return new ResponseEntity<>(existingProject, HttpStatus.OK);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Project> deleteProject(@PathVariable("id") Long id) {
//        Optional<Project> project = projectRepository.findById(id);
//
//        if (project.isEmpty()) {
//            throw new RecordNotFoundException("Could not find any project with id '" + id + "' in database.");
//        } else {
//            Project existingProject = project.get();
//
//            projectRepository.delete(existingProject);
//
//            return new ResponseEntity<>(existingProject, HttpStatus.OK);
//        }
//    }
}

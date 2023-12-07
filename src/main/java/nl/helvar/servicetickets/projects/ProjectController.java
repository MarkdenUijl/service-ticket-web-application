package nl.helvar.servicetickets.projects;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects (
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address
    ) {
        List<ProjectDTO> projectDtos = service.getAllProjects(name, address);

        return new ResponseEntity<>(projectDtos, HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProjectDTO> findProjectById(@PathVariable("id") Long id) {
//        ProjectDTO projectDto = service.findById(id);
//
//        if (projectDto == null) {
//            throw new RecordNotFoundException("Could not find any projects with id " + id + " in the database.");
//        } else {
//            return new ResponseEntity<>(projectDto, HttpStatus.OK);
//        }
//    }

    @PostMapping
    public ResponseEntity<Object> createProject(@Valid @RequestBody ProjectCreationDTO project, BindingResult br) {
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
                            .path("/" + project.getId())
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

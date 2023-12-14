package nl.helvar.servicetickets.projects;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import nl.helvar.servicetickets.helpers.DTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;
    private final DTOValidator dtoValidator = new DTOValidator();

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects (
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String zipCode,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) Integer houseNumber,
            @RequestParam(required = false) Boolean hasServiceContract

    ) {
        List<ProjectDTO> projectDTOS = service.getAllProjects(name, city, zipCode, street, houseNumber, hasServiceContract);

        return new ResponseEntity<>(projectDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> findProjectById(@PathVariable("id") Long id) {
        ProjectDTO projectDto = service.findById(id);

        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectCreationDTO> createProject(@Valid @RequestBody ProjectCreationDTO project, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
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

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> replaceProject(@PathVariable("id") Long id, @Valid @RequestBody ProjectCreationDTO newProject, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            ProjectDTO replacedProject = service.replaceProject(id, newProject);

            return new ResponseEntity<>(replacedProject, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectDTO> deleteProject(@PathVariable("id") Long id) {
        ProjectDTO deletedProject = service.deleteProject(id);

        return new ResponseEntity<>(deletedProject, HttpStatus.OK);
    }
}

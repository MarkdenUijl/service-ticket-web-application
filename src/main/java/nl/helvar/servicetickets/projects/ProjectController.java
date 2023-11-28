package nl.helvar.servicetickets.projects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address
    ) {
        List<Project> filteredProjects = projectRepository.findAllByFilter(name, address);

        if (filteredProjects.isEmpty()) {
            // CREATE EXCEPTION HANDLER HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(filteredProjects, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findProjectById(@PathVariable("id") Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            // CREATE EXCEPTION HANDLER HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(project.get(), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        projectRepository.save(project);

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + project.getId())
                        .toUriString()
        );

        return ResponseEntity.created(uri).body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> replaceProject(@PathVariable("id") Long id, @RequestBody Project newProject) {
        Optional<Project> Project = projectRepository.findById(id);

        if (Project.isEmpty()) {
            // CREATE EXCEPTION HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            Project existingProject = Project.get();

            BeanUtils.copyProperties(newProject, existingProject, "id");

            projectRepository.save(existingProject);

            return new ResponseEntity<>(existingProject, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable("id") Long id) {
        Optional<Project> Project = projectRepository.findById(id);

        if (Project.isEmpty()) {
            // CREATE EXCEPTION HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            Project existingProject = Project.get();

            projectRepository.delete(existingProject);

            return new ResponseEntity<>(existingProject, HttpStatus.OK);
        }
    }
}

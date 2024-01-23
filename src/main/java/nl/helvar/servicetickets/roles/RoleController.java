package nl.helvar.servicetickets.roles;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;
import static nl.helvar.servicetickets.helpers.UriCreator.createUri;

@Controller
@RequestMapping("/roles")
public class RoleController {
    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roleDTOS = service.getAllRoles();

        return new ResponseEntity<>(roleDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findRoleById(@PathVariable("id") Long id) {
        RoleDTO roleDTO = service.findRoleById(id);

        return new ResponseEntity<>(roleDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleCreationDTO role, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            RoleDTO roleOutput = service.createRole(role);

            URI uri = createUri(roleOutput);

            return ResponseEntity.created(uri).body(roleOutput);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> adjustRole(@PathVariable("id")Long id, @Valid @RequestBody RoleCreationDTO role, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            RoleDTO roleOutput = service.adjustRole(id, role);

            return new ResponseEntity<>(roleOutput, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.deleteRole(id), HttpStatus.OK);
    }
}

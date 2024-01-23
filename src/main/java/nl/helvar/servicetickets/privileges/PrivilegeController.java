package nl.helvar.servicetickets.privileges;

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
@RequestMapping("/privileges")
public class PrivilegeController {
    private final PrivilegeService service;

    public PrivilegeController(PrivilegeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PrivilegeDTO>> getAllPrivileges() {
        List<PrivilegeDTO> privilegeDTOS = service.getAllPrivileges();

        return new ResponseEntity<>(privilegeDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrivilegeDTO> findPrivilegeById(@PathVariable("id") Long id) {
        PrivilegeDTO privilegeDTO = service.findPrivilegeById(id);

        return new ResponseEntity<>(privilegeDTO, HttpStatus.OK);
    }
}

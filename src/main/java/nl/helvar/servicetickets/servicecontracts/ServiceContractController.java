package nl.helvar.servicetickets.servicecontracts;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;
import static nl.helvar.servicetickets.helpers.UriCreator.createUri;

@RestController
@RequestMapping("/serviceContracts")
public class ServiceContractController {
    private final ServiceContractService service;

    public ServiceContractController(ServiceContractService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceContractDTO>> getAllServiceContracts(@RequestParam(required = false) String type) {
        List<ServiceContractDTO> serviceContractDTOS = service.getAllServiceContracts(type);

        return new ResponseEntity<>(serviceContractDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceContractDTO> findServiceContractById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ServiceContractDTO> addServiceContract(@Valid @RequestBody ServiceContractCreationDTO serviceContract, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            ServiceContractDTO serviceContractOutput = service.createServiceContract(serviceContract);

            URI uri = createUri(serviceContractOutput);

            return ResponseEntity.created(uri).body(serviceContractOutput);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceContractDTO> replaceServiceContract(@PathVariable("id") Long id, @RequestBody ServiceContractCreationDTO newServiceContract) {
        return new ResponseEntity<>(service.replaceServiceContract(id, newServiceContract), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceContract(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.deleteServiceContract(id), HttpStatus.OK);
    }
}

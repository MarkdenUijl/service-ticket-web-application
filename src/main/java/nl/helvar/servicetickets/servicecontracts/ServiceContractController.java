package nl.helvar.servicetickets.servicecontracts;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.DTOValidator;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;

@RestController
@RequestMapping("/serviceContracts")
public class ServiceContractController {
    private final ServiceContractService service;
    private final DTOValidator dtoValidator = new DTOValidator();

    public ServiceContractController(ServiceContractService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceContractDTO>> getAllServiceContracts(@RequestParam(required = false) String type) {
        List<ServiceContractDTO> serviceContractDTOS = service.getAllServiceContracts(type);

        return new ResponseEntity<>(serviceContractDTOS, HttpStatus.OK);
    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ServiceContract> findServiceContractById(@PathVariable("id") Long id) {
//        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);
//
//        if (serviceContract.isEmpty()) {
//            throw new RecordNotFoundException("Could not find any contract with id '" + id + "' in database.");
//        } else {
//            return new ResponseEntity<>(serviceContract.get(), HttpStatus.OK);
//        }
//    }

    @PostMapping
    public ResponseEntity<ServiceContractCreationDTO> addServiceContract(@Valid @RequestBody ServiceContractCreationDTO serviceContract, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            serviceContract = service.createServiceContract(serviceContract);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + serviceContract.getId())
                            .toUriString()
            );

            return ResponseEntity.created(uri).body(serviceContract);
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ServiceContract> replaceServiceContract(@PathVariable("id") Long id, @RequestBody ServiceContract newServiceContract) {
//        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);
//
//        if (serviceContract.isEmpty()) {
//            throw new RecordNotFoundException("Could not find any contract with id '" + id + "' in database.");
//        } else {
//            ServiceContract existingServiceContract = serviceContract.get();
//
//            BeanUtils.copyProperties(newServiceContract, existingServiceContract, "id");
//
//            serviceContractRepository.save(existingServiceContract);
//
//            return new ResponseEntity<>(existingServiceContract, HttpStatus.OK);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ServiceContract> deleteServiceContract(@PathVariable("id") Long id) {
//        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);
//
//        if (serviceContract.isEmpty()) {
//            throw new RecordNotFoundException("Could not find any contract with id '" + id + "' in database.");
//        } else {
//            ServiceContract existingServiceContract = serviceContract.get();
//
//            serviceContractRepository.delete(existingServiceContract);
//
//            return new ResponseEntity<>(existingServiceContract, HttpStatus.OK);
//        }
//    }
}

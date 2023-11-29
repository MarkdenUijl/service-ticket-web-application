package nl.helvar.servicetickets.servicecontracts;

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
@RequestMapping("/serviceContracts")
public class ServiceContractController {
    @Autowired
    private ServiceContractRepository serviceContractRepository;

    @GetMapping
    public ResponseEntity<List<ServiceContract>> getAllServiceContracts() {
        List<ServiceContract> filteredServiceContracts = serviceContractRepository.findAll();

        if (filteredServiceContracts.isEmpty()) {
            // CREATE EXCEPTION HANDLER HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(filteredServiceContracts, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceContract> findServiceContractById(@PathVariable("id") Long id) {
        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);

        if (serviceContract.isEmpty()) {
            // CREATE EXCEPTION HANDLER HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(serviceContract.get(), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<ServiceContract> addServiceContract(@RequestBody ServiceContract serviceContract) {
        serviceContractRepository.save(serviceContract);

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + serviceContract.getId())
                        .toUriString()
        );

        return ResponseEntity.created(uri).body(serviceContract);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceContract> replaceServiceContract(@PathVariable("id") Long id, @RequestBody ServiceContract newServiceContract) {
        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);

        if (serviceContract.isEmpty()) {
            // CREATE EXCEPTION HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            ServiceContract existingServiceContract = serviceContract.get();

            BeanUtils.copyProperties(newServiceContract, existingServiceContract, "id");

            serviceContractRepository.save(existingServiceContract);

            return new ResponseEntity<>(existingServiceContract, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceContract> deleteServiceContract(@PathVariable("id") Long id) {
        Optional<ServiceContract> serviceContract = serviceContractRepository.findById(id);

        if (serviceContract.isEmpty()) {
            // CREATE EXCEPTION HERE
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            ServiceContract existingServiceContract = serviceContract.get();

            serviceContractRepository.delete(existingServiceContract);

            return new ResponseEntity<>(existingServiceContract, HttpStatus.OK);
        }
    }
}

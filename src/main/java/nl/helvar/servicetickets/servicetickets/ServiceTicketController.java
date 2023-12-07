package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
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
@RequestMapping("/serviceTickets")
public class ServiceTicketController {
    @Autowired
    private ServiceTicketRepository serviceTicketRepository;

    @GetMapping
    public ResponseEntity<List<ServiceTicket>> getAllServiceTickets(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        List<ServiceTicket> filteredServiceTickets = serviceTicketRepository.findAllByFilter(type, status);

        if (filteredServiceTickets.isEmpty()) {
            throw new RecordNotFoundException("Could not find any tickets in database.");
        } else {
            return new ResponseEntity<>(filteredServiceTickets, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTicket> findServiceTicketById(@PathVariable("id") Long id) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            return new ResponseEntity<>(serviceTicket.get(), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<ServiceTicket> addServiceTicket(@RequestBody ServiceTicket serviceTicket) {
        serviceTicketRepository.save(serviceTicket);

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + serviceTicket.getId())
                        .toUriString()
        );

        return ResponseEntity.created(uri).body(serviceTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTicket> replaceServiceTicket(@PathVariable("id") Long id, @RequestBody ServiceTicket newServiceTicket) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();

            BeanUtils.copyProperties(newServiceTicket, existingServiceTicket, "id");

            serviceTicketRepository.save(existingServiceTicket);

            return new ResponseEntity<>(existingServiceTicket, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceTicket> deleteServiceTicket(@PathVariable("id") Long id) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();

            serviceTicketRepository.delete(existingServiceTicket);

            return new ResponseEntity<>(existingServiceTicket, HttpStatus.OK);
        }
    }
}

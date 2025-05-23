package nl.helvar.servicetickets.servicetickets;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.email.EmailService;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;
import static nl.helvar.servicetickets.helpers.UriCreator.createUri;

@RestController
@RequestMapping("/serviceTickets")
public class ServiceTicketController {
    private final ServiceTicketService service;
    private final EmailService emailService;

    public ServiceTicketController(ServiceTicketService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceTicketDTO>> getAllServiceTickets(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) LocalDate issuedBefore,
            @RequestParam(required = false) LocalDate issuedAfter,
            @RequestParam(required = false) String submitterFirstName,
            @RequestParam(required = false) String submitterLastName,
            @RequestParam(required = false) String submitterEmail,
            @RequestParam(required = false) Long submitterId
    ) {
        List<ServiceTicketDTO> serviceTicketDTOS = service.getAllServiceTickets(
                        type,
                        status,
                        projectId,
                        projectName,
                        issuedBefore,
                        issuedAfter,
                        submitterFirstName,
                        submitterLastName,
                        submitterEmail,
                        submitterId
                )
                .stream()
                .map(ServiceTicketDTO::toDto)
                .toList();;

        return new ResponseEntity<>(serviceTicketDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTicketDTO> findServiceTicketById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(ServiceTicketDTO.toDto(service.findById(userDetails, id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ServiceTicketDTO> addServiceTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ServiceTicketCreationDTO serviceTicket, BindingResult br
    ) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            ServiceTicketDTO serviceTicketOutput = service.createServiceTicket(userDetails, serviceTicket);

            URI uri = createUri(serviceTicketOutput);

            try {
                emailService.sendTicketConfirmationEmail(userDetails.getUsername(), serviceTicket.getId(), serviceTicket.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.created(uri).body(serviceTicketOutput);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTicketDTO> replaceServiceTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id,
            @RequestBody ServiceTicketCreationDTO newServiceTicket
    ) {
        return new ResponseEntity<>(ServiceTicketDTO.toDto(service.replaceServiceTicket(userDetails, id, newServiceTicket)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(service.deleteServiceTicket(userDetails, id), HttpStatus.OK);
    }
}

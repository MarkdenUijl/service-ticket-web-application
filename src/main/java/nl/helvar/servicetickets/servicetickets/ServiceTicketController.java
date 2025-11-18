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
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) LocalDate issuedBefore,
            @RequestParam(required = false) LocalDate issuedAfter,
            @RequestParam(required = false) String submitterFirstName,
            @RequestParam(required = false) String submitterLastName,
            @RequestParam(required = false) String submitterEmail,
            @RequestParam(required = false) Long submitterId,
            @RequestParam(required = false, defaultValue = "creationDate") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) Integer limit
    ) {
        List<ServiceTicketDTO> tickets = service.getAllServiceTicketsFiltered(
                        userDetails,
                        type, status, source, priority,
                        projectId, projectName,
                        issuedBefore, issuedAfter,
                        submitterFirstName, submitterLastName,
                        submitterEmail, submitterId,
                        sortBy, sortOrder,
                        limit
                ).stream()
                .map(ServiceTicketDTO::toDto)
                .toList();

        return ResponseEntity.ok(tickets);
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
                emailService.sendTicketConfirmationEmail(
                        serviceTicketOutput.getSubmittedBy().getEmail(),
                        serviceTicketOutput.getId(),
                        serviceTicketOutput.getName()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.created(uri).body(serviceTicketOutput);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceTicketDTO> updateServiceTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody ServiceTicketUpdateDTO updates
    ) {
        ServiceTicketDTO updated = service.updateServiceTicket(userDetails, id, updates);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ServiceTicketDTO> updateTicketStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody TicketStatusUpdateDTO statusUpdate
    ) {
        ServiceTicketDTO updatedTicket = service.updateTicketStatus(userDetails, id, statusUpdate.status());

        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTicketDTO> replaceServiceTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id,
            @RequestBody ServiceTicketCreationDTO newServiceTicket
    ) {
        ServiceTicketDTO updatedTicket = ServiceTicketDTO.toDto(
            service.replaceServiceTicket(userDetails, id, newServiceTicket)
        );

        return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceTicket(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id
    ) {
        String response = service.deleteServiceTicket(userDetails, id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package nl.helvar.servicetickets.ticketresponses;

import jakarta.validation.Valid;
import nl.helvar.servicetickets.exceptions.BadObjectCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static nl.helvar.servicetickets.helpers.DTOValidator.buildErrorMessage;
import static nl.helvar.servicetickets.helpers.UriCreator.createUri;

@RestController
@RequestMapping("/ticketResponses")
public class TicketResponseController {
    private final TicketResponseService service;

    public TicketResponseController(TicketResponseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTicketResponses(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName
    ) {
        List<TicketResponseDTO> ticketResponseDTOS = service.getAllServiceTicketResponses(userId, email, firstName, lastName);

        return new ResponseEntity<>(ticketResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> findTicketResponseById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getTicketResponseById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> addServiceTicketResponse(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TicketResponseCreationDTO ticketResponse, BindingResult br
    ) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            TicketResponseDTO ticketResponseOutput = service.createTicketResponse(userDetails, ticketResponse);

            URI uri = createUri(ticketResponseOutput);

            return ResponseEntity.created(uri).body(ticketResponseOutput);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> replaceServiceTicketResponse(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id,
            @RequestBody TicketResponseCreationDTO newTicketResponse
    ) {
        return new ResponseEntity<>(service.replaceTicketResponse(userDetails, id, newTicketResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceTicketResponse(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(service.deleteTicketResponse(userDetails, id), HttpStatus.OK);
    }
}

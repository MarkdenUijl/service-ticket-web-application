package nl.helvar.servicetickets.ticketresponses;

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
@RequestMapping("/ticketResponses")
public class TicketResponseController {
    private final TicketResponseService service;

    public TicketResponseController(TicketResponseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTicketResponses() {
        List<TicketResponseDTO> ticketResponseDTOS = service.getAllServiceTickets();

        return new ResponseEntity<>(ticketResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> findTicketResponseById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getTicketResponseById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TicketResponseCreationDTO> addServiceTicket(@Valid @RequestBody TicketResponseCreationDTO ticketResponse, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new BadObjectCreationException(buildErrorMessage(br));
        } else {
            ticketResponse = service.createTicketResponse(ticketResponse);

            URI uri = createUri(ticketResponse);

            return ResponseEntity.created(uri).body(ticketResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> replaceServiceTicket(@PathVariable("id") Long id, @RequestBody TicketResponseCreationDTO newTicketResponse) {
        return new ResponseEntity<>(service.replaceTicketResponse(id, newTicketResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteServiceTicket(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.deleteTicketResponse(id), HttpStatus.OK);
    }
}

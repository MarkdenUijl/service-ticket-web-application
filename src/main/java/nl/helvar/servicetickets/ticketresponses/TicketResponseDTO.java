package nl.helvar.servicetickets.ticketresponses;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketDTO;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class TicketResponseDTO implements Identifyable {
    private Long id;
    private String response;
    private LocalDateTime creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public static TicketResponse fromDto(TicketResponseDTO ticketResponseDTO) {
        TicketResponse ticketResponse = new TicketResponse();

        ticketResponse.setResponse(ticketResponseDTO.getResponse());
        ticketResponse.setCreationDate(ticketResponseDTO.getCreationDate());

        return ticketResponse;
    }

    public static TicketResponseDTO toDto(TicketResponse ticketResponse) {
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();

        ticketResponseDTO.setId(ticketResponse.getId());
        ticketResponseDTO.setResponse(ticketResponse.getResponse());
        ticketResponseDTO.setCreationDate(ticketResponse.getCreationDate());

        return ticketResponseDTO;
    }
}

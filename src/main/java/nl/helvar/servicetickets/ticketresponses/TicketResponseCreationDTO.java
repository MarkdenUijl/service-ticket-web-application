package nl.helvar.servicetickets.ticketresponses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class TicketResponseCreationDTO implements Identifyable {
    private Long id;
    @NotBlank
    private String response;
    private LocalDateTime creationDate;
    @NotNull
    private Long serviceTicketId;

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

    public Long getServiceTicketId() {
        return serviceTicketId;
    }

    public void setServiceTicketId(Long serviceTicketId) {
        this.serviceTicketId = serviceTicketId;
    }

    public TicketResponse fromDto(ServiceTicketRepository serviceTicketRepository) {
        TicketResponse ticketResponse = new TicketResponse();

        ticketResponse.setResponse(this.getResponse());
        ticketResponse.setCreationDate(this.getCreationDate());

        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(this.getServiceTicketId());

        if (serviceTicket.isPresent()) {
            ticketResponse.setTicket(serviceTicket.get());
        } else {
            throw new RecordNotFoundException("Could not find ticket with id '" + this.getServiceTicketId() + "' in the database.");
        }

        return ticketResponse;
    }

    public static TicketResponseCreationDTO toDto(TicketResponse ticketResponse) {
        TicketResponseCreationDTO ticketResponseCreationDTO = new TicketResponseCreationDTO();

        ticketResponseCreationDTO.setId(ticketResponse.getId());
        ticketResponseCreationDTO.setResponse(ticketResponse.getResponse());
        ticketResponseCreationDTO.setCreationDate(ticketResponse.getCreationDate());
        ticketResponseCreationDTO.setServiceTicketId(ticketResponse.getTicket().getId());

        return ticketResponseCreationDTO;
    }
}

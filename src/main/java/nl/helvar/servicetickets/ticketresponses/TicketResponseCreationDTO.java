package nl.helvar.servicetickets.ticketresponses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.servicecontracts.ServiceContractRepository;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;
import nl.helvar.servicetickets.ticketresponses.subclasses.EngineerResponse;

import java.time.LocalDateTime;
import java.util.Optional;

public class TicketResponseCreationDTO {
    private Long id;
    @NotBlank
    private String response;
    private LocalDateTime creationDate;
    @NotNull
    private Long serviceTicketId;
    private int minutesSpent;
    private boolean isEngineerResponse;

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

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(int minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public boolean getIsEngineerResponse() {
        return isEngineerResponse;
    }

    public void setIsEngineerResponse(boolean engineerResponse) {
        isEngineerResponse = engineerResponse;
    }

    public TicketResponse fromDto(ServiceTicketRepository serviceTicketRepository) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(this.getServiceTicketId());

        if(serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find ticket with id '" + this.getServiceTicketId() + "' in the database.");
        } else {
            if (this.getIsEngineerResponse()) {
                EngineerResponse engineerResponse = new EngineerResponse();

                engineerResponse.setResponse(this.getResponse());
                engineerResponse.setCreationDate(this.getCreationDate());
                engineerResponse.setMinutesSpent(this.getMinutesSpent());
                engineerResponse.setTicket(serviceTicket.get());

                return engineerResponse;
            } else {
                TicketResponse ticketResponse = new TicketResponse();
                ticketResponse.setResponse(this.getResponse());
                ticketResponse.setCreationDate(this.getCreationDate());
                ticketResponse.setTicket(serviceTicket.get());

                return ticketResponse;
            }
        }
    }

    public TicketResponse fromPutDto() {
        if (this.getIsEngineerResponse() || this.getMinutesSpent() != 0) {
            EngineerResponse engineerResponse = new EngineerResponse();

            engineerResponse.setResponse(this.getResponse());
            engineerResponse.setCreationDate(this.getCreationDate());
            engineerResponse.setMinutesSpent(this.getMinutesSpent());

            return engineerResponse;
        } else {
            TicketResponse ticketResponse = new TicketResponse();
            ticketResponse.setResponse(this.getResponse());
            ticketResponse.setCreationDate(this.getCreationDate());

            return ticketResponse;
        }
    }
}

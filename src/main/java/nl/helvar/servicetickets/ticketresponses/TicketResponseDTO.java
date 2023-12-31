package nl.helvar.servicetickets.ticketresponses;

import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.ticketresponses.subclasses.EngineerResponse;

import java.time.LocalDateTime;

public class TicketResponseDTO implements Identifyable {
    private Long id;
    private String response;
    private LocalDateTime creationDate;
    private int minutesSpent;

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

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(int minutesSpent) {
        this.minutesSpent = minutesSpent;
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

        if (ticketResponse instanceof EngineerResponse engineerResponse) {
            ticketResponseDTO.setMinutesSpent(engineerResponse.getMinutesSpent());
        }

        return ticketResponseDTO;
    }
}

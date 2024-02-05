package nl.helvar.servicetickets.ticketresponses;

import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.users.User;
import nl.helvar.servicetickets.users.UserDTO;

import java.time.LocalDateTime;

public class TicketResponseDTO implements Identifyable {
    private Long id;
    private UserDTO submittedBy;
    private String response;
    private LocalDateTime creationDate;
    private boolean isEngineerResponse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(UserDTO submittedBy) {
        this.submittedBy = submittedBy;
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

    public boolean isEngineerResponse() {
        return isEngineerResponse;
    }

    public void setEngineerResponse(boolean engineerResponse) {
        isEngineerResponse = engineerResponse;
    }

    public static TicketResponseDTO toDto(TicketResponse ticketResponse) {
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        User submittingUser = ticketResponse.getSubmittedBy();

        ticketResponseDTO.setId(ticketResponse.getId());
        ticketResponseDTO.setSubmittedBy(UserDTO.toSimpleDto(submittingUser));
        ticketResponseDTO.setResponse(ticketResponse.getResponse());
        ticketResponseDTO.setCreationDate(ticketResponse.getCreationDate());
        ticketResponseDTO.setEngineerResponse(submittingUser.hasPrivilege("CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE"));

        return ticketResponseDTO;
    }
}

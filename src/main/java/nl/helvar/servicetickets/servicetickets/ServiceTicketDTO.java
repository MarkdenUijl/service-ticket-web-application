package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import nl.helvar.servicetickets.ticketresponses.TicketResponseDTO;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class ServiceTicketDTO {
    private Long id;
    private String name;
    private TicketStatus status;
    private TicketType type;
    private String description;
    private List<TicketResponseDTO> responses;
    private int minutesSpent;
    private LocalDateTime creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TicketResponseDTO> getResponses() {
        return responses;
    }

    public void setResponses(List<TicketResponseDTO> responses) {
        this.responses = responses;
    }

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(int minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public static ServiceTicket fromDto(ServiceTicketDTO serviceTicketDTO) {
        ServiceTicket serviceTicket = new ServiceTicket();

        serviceTicket.setName(serviceTicketDTO.getName());
        serviceTicket.setStatus(serviceTicketDTO.getStatus());
        serviceTicket.setType(serviceTicketDTO.getType());
        serviceTicket.setDescription(serviceTicketDTO.getDescription());
        serviceTicket.setMinutesSpent(serviceTicketDTO.getMinutesSpent());
        serviceTicket.setCreationDate(serviceTicketDTO.getCreationDate());
        serviceTicket.setResponses(serviceTicketDTO.getResponses()
                .stream()
                .map(TicketResponseDTO::fromDto)
                .toList()
        );

        return serviceTicket;
    }

    public static ServiceTicketDTO toDto(ServiceTicket serviceTicket) {
        ServiceTicketDTO serviceTicketDTO = new ServiceTicketDTO();

        serviceTicketDTO.setId(serviceTicket.getId());
        serviceTicketDTO.setName(serviceTicket.getName());
        serviceTicketDTO.setStatus(serviceTicket.getStatus());
        serviceTicketDTO.setType(serviceTicket.getType());
        serviceTicketDTO.setDescription(serviceTicket.getDescription());
        serviceTicketDTO.setMinutesSpent(serviceTicket.getMinutesSpent());
        serviceTicketDTO.setCreationDate(serviceTicket.getCreationDate());
        serviceTicketDTO.setResponses(serviceTicket.getResponses()
                .stream()
                .map(TicketResponseDTO::toDto)
                .sorted(Comparator.comparing(TicketResponseDTO::getCreationDate))
                .toList()
        );

        return serviceTicketDTO;
    }
}

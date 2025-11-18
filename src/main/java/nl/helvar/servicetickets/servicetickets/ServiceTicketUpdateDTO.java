package nl.helvar.servicetickets.servicetickets;

import jakarta.validation.constraints.Size;
import nl.helvar.servicetickets.servicetickets.enums.TicketPriority;
import nl.helvar.servicetickets.servicetickets.enums.TicketSource;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import nl.helvar.servicetickets.ticketresponses.TicketResponse;

import java.time.Instant;
import java.util.List;

import static nl.helvar.servicetickets.helpers.EnumValidator.getEnumConstantFromString;

public class ServiceTicketUpdateDTO {
    private String name;
    private String type;
    @Size(max = 5000)
    private String description;
    private Long projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TicketType getType() {
        return (TicketType) getEnumConstantFromString(TicketType.values(), this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


}

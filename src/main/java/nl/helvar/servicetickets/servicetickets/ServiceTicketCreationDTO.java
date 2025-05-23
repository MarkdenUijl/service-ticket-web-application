package nl.helvar.servicetickets.servicetickets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.projects.ProjectRepository;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import nl.helvar.servicetickets.ticketresponses.TicketResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.EnumValidator.getEnumConstantFromString;

public class ServiceTicketCreationDTO {
    private Long id;
    @Size(min = 10)
    private String name;
    @NotNull
    private String status;
    @NotNull
    private String type;
    @NotBlank
    private String description;
    private List<TicketResponse> responses;
    private int minutesSpent;
    private LocalDateTime creationDate;
    private Long projectId;

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
        return (TicketStatus) getEnumConstantFromString(TicketStatus.values(), this.status);
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<TicketResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<TicketResponse> responses) {
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


    public ServiceTicket fromDto(ProjectRepository projectRepository) {
        ServiceTicket serviceTicket = new ServiceTicket();

        serviceTicket.setName(this.getName());
        serviceTicket.setStatus(this.getStatus());
        serviceTicket.setType(this.getType());
        serviceTicket.setDescription(this.getDescription());
        serviceTicket.setResponses(this.getResponses());
        serviceTicket.setMinutesSpent(this.getMinutesSpent());
        serviceTicket.setCreationDate(this.getCreationDate());

        if (this.getProjectId() != null) {
            Optional<Project> project = projectRepository.findById(this.getProjectId());

            if (project.isPresent()) {
                serviceTicket.setProject(project.get());
            } else {
                throw new RecordNotFoundException("Project with id '" + this.getProjectId() + "' not found.");
            }
        }

        return serviceTicket;
    }
}

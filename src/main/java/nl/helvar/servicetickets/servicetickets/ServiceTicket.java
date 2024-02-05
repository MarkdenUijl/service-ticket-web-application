package nl.helvar.servicetickets.servicetickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import nl.helvar.servicetickets.files.File;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import nl.helvar.servicetickets.ticketresponses.TicketResponse;
import nl.helvar.servicetickets.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "service_tickets")
public class ServiceTicket {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private TicketStatus status;
    private TicketType type;
    private String description;
    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<TicketResponse> responses;
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
    private int minutesSpent;
    private LocalDateTime creationDate;
    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<File> files;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User submittedBy;

    public Long getId() {
        return id;
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

    public List<TicketResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<TicketResponse> responses) {
        this.responses = responses;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public User getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(User submittedBy) {
        this.submittedBy = submittedBy;
    }

    public boolean hasFileById(Long id) {
        for (File file : this.files) {
            if (Objects.equals(file.getId(), id)) {
                return true;
            }
        }

        return false;
    }
}

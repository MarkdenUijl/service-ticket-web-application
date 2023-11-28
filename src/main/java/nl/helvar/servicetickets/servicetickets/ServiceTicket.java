package nl.helvar.servicetickets.servicetickets;

import jakarta.persistence.*;
import nl.helvar.servicetickets.reports.Report;

import java.util.List;

@Entity
@Table(name = "service_tickets")
public class ServiceTicket {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type; //ENUM MAKEN!!!
    private String status; //ENUM MAKEN!!!
    // private List<Media> media;
    private String description;
    @OneToMany
    private List<Report> reports;
    // private User submittedBy
    @Column(name = "minutes_spent")
    private int minutesSpent;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(int minutesSpent) {
        this.minutesSpent = minutesSpent;
    }
}

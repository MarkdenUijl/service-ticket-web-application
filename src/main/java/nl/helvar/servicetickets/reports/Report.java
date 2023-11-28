package nl.helvar.servicetickets.reports;

import jakarta.persistence.*;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    @Column(name = "time_spent")
    private int timeSpent;
    // private User createdBy;


    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }
}

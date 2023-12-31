package nl.helvar.servicetickets.ticketresponses;

import jakarta.persistence.*;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "response_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("basic_response")
@Table(name = "ticket_responses")
public class TicketResponse {
    @Id
    @GeneratedValue
    private Long id;
    private String response;
    //@OneToOne
    //private User createdBy; UITWERKEN!!!
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private ServiceTicket ticket;

    public Long getId() {
        return id;
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

    public ServiceTicket getTicket() {
        return ticket;
    }

    public void setTicket(ServiceTicket ticket) {
        this.ticket = ticket;
    }
}

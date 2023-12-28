package nl.helvar.servicetickets.ticketresponses;

import jakarta.persistence.*;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.users.User;
import org.hibernate.engine.internal.Cascade;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_responses")
public class TicketResponse {
    @Id
    @GeneratedValue
    private Long id;
    private String responseBody;
    //@OneToOne
    //private User createdBy; UITWERKEN!!!
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private ServiceTicket ticket;
}

package nl.helvar.servicetickets.files;

import jakarta.persistence.*;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import org.hibernate.annotations.Type;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Lob
    private byte[] data;

    // Relations:
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private ServiceTicket ticket;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ServiceTicket getTicket() {
        return ticket;
    }

    public void setTicket(ServiceTicket ticket) {
        this.ticket = ticket;
    }
}

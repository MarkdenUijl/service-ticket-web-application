package nl.helvar.servicetickets.projects;

import jakarta.persistence.*;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String address;
    @OneToMany(mappedBy = "project")
    private Set<ServiceTicket> tickets = new HashSet<>();
    @OneToOne
    @JoinColumn
    private ServiceContract serviceContract;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<ServiceTicket> getTickets() {
        return tickets;
    }

    public ServiceContract getServiceContract() {
        return serviceContract;
    }
}

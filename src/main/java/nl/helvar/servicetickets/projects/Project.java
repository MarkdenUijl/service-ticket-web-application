package nl.helvar.servicetickets.projects;

import jakarta.persistence.*;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String address;
//    @OneToMany
//    private List<ServiceTicket> tickets = new ArrayList<>();
//    @OneToOne
//    @Column(name = "service_contract")
//    private ServiceContract serviceContract;


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

}

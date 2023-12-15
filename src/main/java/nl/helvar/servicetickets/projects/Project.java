package nl.helvar.servicetickets.projects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import org.springframework.lang.Nullable;

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
    private String city;
    private String zipCode;
    private String street;
    private int houseNumber;

    // Relations
    @OneToMany(mappedBy = "project")
    private Set<ServiceTicket> tickets = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Set<ServiceTicket> getTickets() {
        return tickets;
    }

    public ServiceContract getServiceContract() {
        return serviceContract;
    }

    public void setServiceContract(ServiceContract serviceContract) {
        this.serviceContract = serviceContract;
    }
}

package nl.helvar.servicetickets.projects;

import jakarta.persistence.*;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;

import java.util.List;

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
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ServiceTicket> tickets;
    @OneToOne(cascade = CascadeType.ALL)
    private ServiceContract serviceContract;

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

    public List<ServiceTicket> getTickets() {
        return tickets;
    }

    public void addServiceTicket(ServiceTicket ticket) {
        tickets.add(ticket);
    }

    public ServiceContract getServiceContract() {
        return serviceContract;
    }

    public void setServiceContract(ServiceContract serviceContract) {
        this.serviceContract = serviceContract;
    }
}

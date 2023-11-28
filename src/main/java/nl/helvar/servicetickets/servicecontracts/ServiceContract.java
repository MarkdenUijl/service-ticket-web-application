package nl.helvar.servicetickets.servicecontracts;

import jakarta.persistence.*;

@Entity
@Table(name = "service_contracts")
public class ServiceContract {
    @Id
    @GeneratedValue
    private Long id;
    private String type; //ENUM MAKEN!!!
    @Column(name = "available_time")
    private int availableTime;
    @Column(name = "used_time")
    private int usedTime;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(int availableTime) {
        this.availableTime = availableTime;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }
}

package nl.helvar.servicetickets.servicecontracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;

import java.time.LocalDate;

@Entity
@Table(name = "service_contracts")
public class ServiceContract {
    @Id
    @GeneratedValue
    private Long id;
    private ContractType type;
    private int contractTimeInMinutes;
    private int usedTime;
    private LocalDate startDate;
    private LocalDate endDate;

    // Relations
    @OneToOne(mappedBy = "serviceContract")
    @JsonIgnore
    private Project project;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public ContractType getType() {
        return type;
    }

    public void setType(ContractType type) {
        this.type = type;
    }

    public int getContractTime() {
        return contractTimeInMinutes;
    }

    public void setContractTime(int contractTimeInMinutes) {
        this.contractTimeInMinutes = contractTimeInMinutes;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

    public void addUsedTime(int time) {
        this.usedTime += time;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

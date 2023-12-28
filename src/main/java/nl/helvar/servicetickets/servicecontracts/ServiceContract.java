package nl.helvar.servicetickets.servicecontracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import nl.helvar.servicetickets.interfaces.Identifyable;
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
    private int contractTime;
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
        return contractTime;
    }

    public void setContractTime(int contractTime) {
        this.contractTime = contractTime;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
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

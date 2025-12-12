package nl.helvar.servicetickets.servicecontracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;
import org.springframework.cglib.core.Local;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_contract_id")
    @JsonIgnore
    private ServiceContract previousContract;

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

    public ServiceContract getPreviousContract() {
        return previousContract;
    }

    public void setPreviousContract(ServiceContract previousContract) {
        this.previousContract = previousContract;
    }

    public boolean isValid() {
        LocalDate today = LocalDate.now();

        return !today.isBefore(startDate)
                && !today.isAfter(endDate);
    }
}

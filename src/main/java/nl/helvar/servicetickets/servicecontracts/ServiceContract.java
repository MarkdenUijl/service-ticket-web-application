package nl.helvar.servicetickets.servicecontracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "service_contracts")
public class ServiceContract {
    @Id
    @GeneratedValue
    private Long id;
    private ContractType type;
    @Column(name = "contract_time")
    private int contractTime;
    @Column(name = "used_time")
    @Nullable
    private int usedTime;

    // Relations
    @OneToOne(mappedBy = "serviceContract")
    @JsonIgnore
    private Project project;

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
}

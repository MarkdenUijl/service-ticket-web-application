package nl.helvar.servicetickets.servicecontracts;

import jakarta.validation.constraints.NotNull;
import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;

import java.time.LocalDate;

import static nl.helvar.servicetickets.helpers.EnumValidator.getEnumConstantFromString;

public class ServiceContractCreationDTO implements Identifyable {
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private int contractTime;
    private int usedTime;
    @NotNull
    private Long projectId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContractType getType() {
        return (ContractType) getEnumConstantFromString(ContractType.values(), this.type);
    }

    public void setType(String contractType) {
        this.type = contractType;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    // MAPPERS:
    public ServiceContract fromDto() {
        ServiceContract serviceContract = new ServiceContract();

        serviceContract.setType(this.getType());
        serviceContract.setContractTime(this.getContractTime());
        serviceContract.setUsedTime(this.getUsedTime());
        serviceContract.setStartDate(this.getStartDate());
        serviceContract.setEndDate(this.getEndDate());

        return serviceContract;
    }

    public ServiceContractCreationDTO toDto(ServiceContract serviceContract) {
        ServiceContractCreationDTO serviceContractCreationDTO = new ServiceContractCreationDTO();

        serviceContractCreationDTO.setId(serviceContract.getId());
        serviceContractCreationDTO.setType(serviceContract.getType().toString());
        serviceContractCreationDTO.setContractTime(serviceContract.getContractTime());
        serviceContractCreationDTO.setUsedTime(serviceContract.getUsedTime());
        serviceContractCreationDTO.setStartDate(serviceContract.getStartDate());
        serviceContractCreationDTO.setEndDate(serviceContract.getEndDate());

        return serviceContractCreationDTO;
    }
}

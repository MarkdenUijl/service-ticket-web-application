package nl.helvar.servicetickets.servicecontracts;

import jakarta.validation.constraints.NotBlank;
import nl.helvar.servicetickets.exceptions.InvalidEnumConstantException;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;

import static nl.helvar.servicetickets.helpers.EnumValidator.getEnumConstantFromString;

public class ServiceContractCreationDTO {
    private Long id;
    @NotBlank
    private String contractType;
    @NotBlank
    private int contractTime;
    private int usedTime;
    @NotBlank
    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContractType getType() {
        return (ContractType) getEnumConstantFromString(ContractType.values(), this.contractType);
    }

    public void setType(String contractType) {
        this.contractType = contractType;
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

    // MAPPERS:
    public ServiceContract fromDto() {
        ServiceContract serviceContract = new ServiceContract();

        serviceContract.setType(this.getType());
        serviceContract.setContractTime(this.getContractTime());
        serviceContract.setUsedTime(this.getUsedTime());

        return serviceContract;
    }

    public ServiceContractCreationDTO toDto(ServiceContract serviceContract) {
        ServiceContractCreationDTO serviceContractCreationDTO = new ServiceContractCreationDTO();

        serviceContractCreationDTO.setId(serviceContract.getId());
        serviceContractCreationDTO.setType(serviceContract.getType().toString());
        serviceContractCreationDTO.setContractTime(serviceContract.getContractTime());
        serviceContractCreationDTO.setUsedTime(serviceContract.getUsedTime());

        return serviceContractCreationDTO;
    }
}

package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.exceptions.InvalidEnumConstantException;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;

import static nl.helvar.servicetickets.helpers.EnumValidator.getEnumConstantFromString;

public class ServiceContractCreationDTO {
    private Long id;
    private String contractType;
    private int contractTime;
    private int usedTime;

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

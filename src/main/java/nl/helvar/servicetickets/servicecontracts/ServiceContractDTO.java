package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.servicecontracts.enums.ContractType;

public class ServiceContractDTO {
    private Long id;
    private ContractType contractType;
    private int contractTime;
    private int usedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContractType getType() {
        return contractType;
    }

    public void setType(ContractType contractType) {
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
    public static ServiceContract fromDto(ServiceContractDTO serviceContractDTO) {
        ServiceContract serviceContract = new ServiceContract();

        serviceContract.setType(serviceContractDTO.getType());
        serviceContract.setContractTime(serviceContractDTO.getContractTime());
        serviceContract.setUsedTime(serviceContractDTO.getUsedTime());

        return serviceContract;
    }

    public static ServiceContractDTO toDto(ServiceContract serviceContract) {
        ServiceContractDTO serviceContractDTO = new ServiceContractDTO();

        serviceContractDTO.setId(serviceContract.getId());
        serviceContractDTO.setType(serviceContract.getType());
        serviceContractDTO.setContractTime(serviceContract.getContractTime());
        serviceContractDTO.setUsedTime(serviceContract.getUsedTime());

        return serviceContractDTO;
    }
}

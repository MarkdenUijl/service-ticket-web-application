package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.servicecontracts.enums.ContractType;

import java.time.LocalDate;

public class ServiceContractDTO {
    private Long id;
    private ContractType contractType;
    private int contractTime;
    private int usedTime;
    private LocalDate startDate;
    private LocalDate endDate;

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
    public static ServiceContract fromDto(ServiceContractDTO serviceContractDTO) {
        ServiceContract serviceContract = new ServiceContract();

        serviceContract.setType(serviceContractDTO.getType());
        serviceContract.setContractTime(serviceContractDTO.getContractTime());
        serviceContract.setUsedTime(serviceContractDTO.getUsedTime());
        serviceContract.setStartDate(serviceContractDTO.getStartDate());
        serviceContract.setEndDate(serviceContractDTO.getEndDate());

        return serviceContract;
    }

    public static ServiceContractDTO toDto(ServiceContract serviceContract) {
        ServiceContractDTO serviceContractDTO = new ServiceContractDTO();

        serviceContractDTO.setId(serviceContract.getId());
        serviceContractDTO.setType(serviceContract.getType());
        serviceContractDTO.setContractTime(serviceContract.getContractTime());
        serviceContractDTO.setUsedTime(serviceContract.getUsedTime());
        serviceContractDTO.setStartDate(serviceContract.getStartDate());
        serviceContractDTO.setEndDate(serviceContract.getEndDate());

        return serviceContractDTO;
    }
}

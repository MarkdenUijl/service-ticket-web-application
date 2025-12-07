package nl.helvar.servicetickets.servicecontracts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;

public class ServiceContractRenewDTO {

    @NotNull
    private ContractType type;

    @Min(1)
    private Integer hours; // 8/12/16/20/24

    public ContractType getType() {
        return type;
    }

    public void setType(ContractType type) {
        this.type = type;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}
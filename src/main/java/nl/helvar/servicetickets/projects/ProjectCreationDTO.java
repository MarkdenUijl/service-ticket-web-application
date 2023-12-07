package nl.helvar.servicetickets.projects;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public class ProjectCreationDTO {

    private Long id;
    @Size(min = 5, max = 128)
    private String name;
    @Size(min = 5, max = 128)
    @Pattern(regexp = "^\\s*\\w+(\\s+\\w+)*\\s+\\d+,\\s+\\w+(\\s+\\w+)*$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String address;
    @Nullable
    private Long serviceContractId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getServiceContractId() {
        return serviceContractId;
    }

    public void setServiceContractId(Long serviceContractId) {
        this.serviceContractId = serviceContractId;
    }
}

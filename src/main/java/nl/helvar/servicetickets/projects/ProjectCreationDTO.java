package nl.helvar.servicetickets.projects;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicecontracts.ServiceContractRepository;

import java.util.Optional;

public class ProjectCreationDTO {
    private Long id;
    @Size(min = 5, max = 128)
    private String name;
    private String city;
    @Pattern(regexp = "\\b\\d{4}\\s?[A-Z]{2}\\b", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid postal code entered, enter 4 numbers followed by 2 capitalized letters")
    private String zipCode;
    private String street;
    private int houseNumber;
    private Long serviceContractId;

    // GETTERS AND SETTERS:
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setServiceContractId(Long serviceContractId) {
        this.serviceContractId = serviceContractId;
    }

    public Long getServiceContractId() {
        return serviceContractId;
    }

    // MAPPERS:
    public Project fromDto(ServiceContractRepository serviceContractRepository) {
        Project project = new Project();

        project.setName(this.getName());
        project.setCity(this.getCity());
        project.setZipCode(this.getZipCode());
        project.setStreet(this.getStreet());
        project.setHouseNumber(this.getHouseNumber());

        if (this.getServiceContractId() != null) {
            Optional<ServiceContract> serviceContract = serviceContractRepository.findById(this.getServiceContractId());

            if (serviceContract.isPresent()) {
                project.setServiceContract(serviceContract.get());
            } else {
                throw new RecordNotFoundException("Service contract with ID " + this.getServiceContractId() + " not found.");
            }
        }

        return project;
    }
}

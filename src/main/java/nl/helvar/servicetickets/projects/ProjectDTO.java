package nl.helvar.servicetickets.projects;

import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.servicecontracts.ServiceContractDTO;
import nl.helvar.servicetickets.servicetickets.ServiceTicketDTO;

import java.util.List;

public class ProjectDTO implements Identifyable {
    private Long id;
    private String name;
    private String city;
    private String zipCode;
    private String street;
    private int houseNumber;
    private ServiceContractDTO serviceContract;
    private List<ServiceTicketDTO> tickets;

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

    public ServiceContractDTO getServiceContract() {
        return serviceContract;
    }

    public void setServiceContract(ServiceContractDTO serviceContract) {
        this.serviceContract = serviceContract;
    }

    public List<ServiceTicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<ServiceTicketDTO> tickets) {
        this.tickets = tickets;
    }

    // MAPPERS:

    public static ProjectDTO toDto(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setCity(project.getCity());
        projectDTO.setZipCode(project.getZipCode());
        projectDTO.setStreet(project.getStreet());
        projectDTO.setHouseNumber(project.getHouseNumber());

        if (project.getServiceContract() != null) {
            projectDTO.setServiceContract(ServiceContractDTO.toDto(project.getServiceContract()));
        }

        if (project.getTickets() != null) {
            projectDTO.setTickets(project.getTickets()
                    .stream()
                    .map(ServiceTicketDTO::toDto)
                    .toList()
            );
        }

        return projectDTO;
    }

    public static ProjectDTO toSimpleDto(Project project) {
        ProjectDTO projectSimpleDTO = new ProjectDTO();

        projectSimpleDTO.setId(project.getId());
        projectSimpleDTO.setName(project.getName());
        projectSimpleDTO.setCity(project.getCity());
        projectSimpleDTO.setZipCode(project.getZipCode());
        projectSimpleDTO.setStreet(project.getStreet());
        projectSimpleDTO.setHouseNumber(project.getHouseNumber());

        return projectSimpleDTO;
    }
}

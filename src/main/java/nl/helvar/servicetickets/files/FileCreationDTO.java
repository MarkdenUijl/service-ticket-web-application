package nl.helvar.servicetickets.files;

import jakarta.validation.constraints.NotNull;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.interfaces.Identifyable;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;

import java.util.Optional;

public class FileCreationDTO {
    private Long id;
    private String name;
    @NotNull
    private Long ticketId;
    private byte[] data;

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

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public File fromDto(ServiceTicketRepository serviceTicketRepository) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(this.getTicketId());

        if(serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find ticket with id '" + this.getTicketId() + "' in the database.");
        }

        File file = new File();

        file.setName(this.getName());
        file.setTicket(serviceTicket.get());
        file.setData(this.getData());

        return file;
    }

    public static FileCreationDTO toDto(File file) {
        FileCreationDTO fileCreationDTO = new FileCreationDTO();

        fileCreationDTO.setId(file.getId());
        fileCreationDTO.setName(file.getName());
        fileCreationDTO.setTicketId(file.getTicket().getId());

        return fileCreationDTO;
    }
}

package nl.helvar.servicetickets.files;

import nl.helvar.servicetickets.configurations.websocket.TicketUpdateNotifier;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketDTO;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final ServiceTicketRepository serviceTicketRepository;
    private final TicketUpdateNotifier ticketUpdateNotifier;

    public FileService(
                FileRepository fileRepository,
                ServiceTicketRepository serviceTicketRepository,
                TicketUpdateNotifier ticketUpdateNotifier
    ) {
        this.fileRepository = fileRepository;
        this.serviceTicketRepository = serviceTicketRepository;
        this.ticketUpdateNotifier = ticketUpdateNotifier;
    }

    public String storeFile(FileCreationDTO fileCreationDTO) {
        File file = fileCreationDTO.fromDto(serviceTicketRepository);

        fileRepository.save(file);

        // Notify frontend of update
        ServiceTicketDTO ticket = ServiceTicketDTO.toDto(file.getTicket());
        ticketUpdateNotifier.broadcastTicketUpdate(ticket);

        return "File with name '" + fileCreationDTO.getName() + "' stored to ticket with id '" + fileCreationDTO.getTicketId() +"' at file id '" + file.getId() + "'.";
    }

    public File getFile(Long id) {
        Optional<File> file = fileRepository.findById(id);

        if (file.isEmpty()) {
            throw new RecordNotFoundException("Could not find any file with id '" + id + "' in database.");
        } else {
            return file.get();
        }
    }

    public String deleteFile(Long id) {
        Optional<File> optionalFile = fileRepository.findById(id);

        if (optionalFile.isEmpty()) {
            throw new RecordNotFoundException("Could not find any file with id '" + id + "' in database.");
        } else {
            File file = optionalFile.get();
            ServiceTicket ticket = file.getTicket();

            ticket.removeFile(file);
            fileRepository.delete(file);

            // Notify frontend of update
            ticketUpdateNotifier.broadcastTicketUpdate(ServiceTicketDTO.toDto(ticket));

            return "File with id '" + id + "' was successfully deleted.";
        }
    }
}

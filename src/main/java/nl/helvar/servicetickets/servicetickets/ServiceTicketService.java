package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.projects.ProjectRepository;
import nl.helvar.servicetickets.servicecontracts.ServiceContractCreationDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.servicetickets.ServiceTicketSpecification.*;

@Service
public class ServiceTicketService {
    private final ServiceTicketRepository serviceTicketRepository;
    private final ProjectRepository projectRepository;

    public ServiceTicketService(ServiceTicketRepository serviceTicketRepository, ProjectRepository projectRepository) {
        this.serviceTicketRepository = serviceTicketRepository;
        this.projectRepository = projectRepository;
    }

    public ServiceTicketDTO createServiceTicket(ServiceTicketCreationDTO serviceTicketCreationDTO) {
        // LATER UITBREIDEN MET RELATIONS, KOPPELEN AAN USER
        LocalDateTime currentTime = LocalDateTime.now();
        serviceTicketCreationDTO.setCreationDate(currentTime);

        ServiceTicket serviceTicket = serviceTicketCreationDTO.fromDto(projectRepository);

        addServiceTicketToProject(serviceTicket);
        serviceTicketRepository.save(serviceTicket);

        return ServiceTicketDTO.toDto(serviceTicket);
    }

    public List<ServiceTicket> getAllServiceTickets(String type, String status, Long projectId, LocalDate issuedBefore, LocalDate issuedAfter) {
        Specification<ServiceTicket> filters = Specification.where(StringUtils.isBlank(type) ? null : typeEquals(type))
                .and(StringUtils.isBlank(status) ? null : statusEquals(status))
                .and(projectId == null ? null : projectEquals(projectId))
                .and(ServiceTicketSpecification.dateRange(issuedAfter, issuedBefore));

        List<ServiceTicket> serviceTickets = serviceTicketRepository.findAll(filters);

        if (serviceTickets.isEmpty()) {
            throw new RecordNotFoundException("Could not find service tickets with these parameters in the database.");
        } else {
            return serviceTickets;
        }
    }

    public ServiceTicket findById(Long id) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            return serviceTicket.get();
        }
    }

    public ServiceTicket replaceServiceTicket(Long id, ServiceTicketCreationDTO newServiceTicket) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();
            ServiceTicket newTicket = newServiceTicket.fromDto(projectRepository);

            ObjectCopyUtils.copyNonNullProperties(newTicket, existingServiceTicket);

            addServiceTicketToProject(existingServiceTicket);

            serviceTicketRepository.save(existingServiceTicket);

            return existingServiceTicket;
        }
    }

    public String deleteServiceTicket(Long id) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();

            serviceTicketRepository.delete(existingServiceTicket);

            return "Service ticket with id '" + id + "' was successfully deleted.";
        }
    }

    // HELPER METHODS
    private void addServiceTicketToProject(ServiceTicket serviceTicket) {
        if (serviceTicket.getProject() == null) {
            throw new RecordNotFoundException("No project connected to the service ticket was found.");
        } else {
            Project project = serviceTicket.getProject();

            project.addServiceTicket(serviceTicket);

            projectRepository.save(project);
        }
    }
}

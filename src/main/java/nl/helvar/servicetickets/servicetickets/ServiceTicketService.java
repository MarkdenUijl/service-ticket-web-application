package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.projects.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.servicetickets.ServiceTicketSpecification.statusEquals;
import static nl.helvar.servicetickets.servicetickets.ServiceTicketSpecification.typeEquals;

@Service
public class ServiceTicketService {
    private final ServiceTicketRepository serviceTicketRepository;
    private final ProjectRepository projectRepository;

    public ServiceTicketService(ServiceTicketRepository serviceTicketRepository, ProjectRepository projectRepository) {
        this.serviceTicketRepository = serviceTicketRepository;
        this.projectRepository = projectRepository;
    }

    public ServiceTicketCreationDTO createServiceTicket(ServiceTicketCreationDTO serviceTicketCreationDTO) {
        // LATER UITBREIDEN MET RELATIONS, KOPPELEN AAN USER
        LocalDateTime currentTime = LocalDateTime.now();
        serviceTicketCreationDTO.setCreationDate(currentTime);

        ServiceTicket serviceTicket = serviceTicketCreationDTO.fromDto(projectRepository);

        if (serviceTicketCreationDTO.getProjectId() != null) {
            Optional<Project> projectOptional = projectRepository.findById(serviceTicketCreationDTO.getProjectId());

            if (projectOptional.isEmpty()) {
                throw new RecordNotFoundException("Project with id " + serviceTicketCreationDTO.getProjectId() + " was not found.");
            } else {
                Project project = projectOptional.get();

                project.addServiceTicket(serviceTicket);

                projectRepository.save(project);
            }
        }

        serviceTicketRepository.save(serviceTicket);

        serviceTicketCreationDTO.setId(serviceTicket.getId());
        return serviceTicketCreationDTO;
    }

    public List<ServiceTicketDTO> getAllServiceTickets(String type, String status) {
        Specification<ServiceTicket> filters = Specification.where(StringUtils.isBlank(type) ? null : typeEquals(type))
                .and(StringUtils.isBlank(status) ? null : statusEquals(status));

        List<ServiceTicketDTO> serviceTickets = serviceTicketRepository.findAll(filters)
                .stream()
                .map(ServiceTicketDTO::toDto)
                .toList();

        if (serviceTickets.isEmpty()) {
            throw new RecordNotFoundException("Could not find service tickets with these parameters in the database.");
        } else {
            return serviceTickets;
        }
    }

    public ServiceTicketDTO findById(Long id) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            return ServiceTicketDTO.toDto(serviceTicket.get());
        }
    }

    public ServiceTicketDTO replaceServiceTicket(Long id, ServiceTicketCreationDTO newServiceTicket) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();

            BeanUtils.copyProperties(newServiceTicket, existingServiceTicket, "id");

            serviceTicketRepository.save(existingServiceTicket);

            return ServiceTicketDTO.toDto(existingServiceTicket);
        }
    }

    public ServiceTicketDTO deleteServiceTicket(Long id) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();

            serviceTicketRepository.delete(existingServiceTicket);

            return ServiceTicketDTO.toDto(existingServiceTicket);
        }
    }
}

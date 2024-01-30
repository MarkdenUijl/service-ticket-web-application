package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.projects.ProjectRepository;
import nl.helvar.servicetickets.users.User;
import nl.helvar.servicetickets.users.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.UserDetailsValidator.hasPrivilege;
import static nl.helvar.servicetickets.servicetickets.ServiceTicketSpecification.*;
import static nl.helvar.servicetickets.users.UserSpecification.emailEquals;

@Service
public class ServiceTicketService {
    private final ServiceTicketRepository serviceTicketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ServiceTicketService(
            ServiceTicketRepository serviceTicketRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.serviceTicketRepository = serviceTicketRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ServiceTicketDTO createServiceTicket(UserDetails userDetails, ServiceTicketCreationDTO serviceTicketCreationDTO) {
        Specification<User> filters = Specification.where(emailEquals(userDetails.getUsername()));
        Optional<User> user = userRepository.findOne(filters);

        if (user.isEmpty()) {
            throw new RecordNotFoundException("Could not find user with email '" + userDetails.getUsername() + "' in database.");
        } else {
            LocalDateTime currentTime = LocalDateTime.now();
            serviceTicketCreationDTO.setCreationDate(currentTime);

            ServiceTicket serviceTicket = serviceTicketCreationDTO.fromDto(projectRepository);
            serviceTicket.setSubmittedBy(user.get());

            serviceTicketRepository.save(serviceTicket);

            return ServiceTicketDTO.toDto(serviceTicket);
        }
    }

    public List<ServiceTicket> getAllServiceTickets(
            String type,
            String status,
            Long projectId,
            String projectName,
            LocalDate issuedBefore,
            LocalDate issuedAfter,
            String submitterFirstName,
            String submitterLastName,
            String submitterEmail,
            Long submitterId
    ) {
        Specification<ServiceTicket> filters = Specification.where(StringUtils.isBlank(type) ? null : typeEquals(type))
                .and(StringUtils.isBlank(status) ? null : statusEquals(status))
                .and(projectId == null ? null : projectIdEquals(projectId))
                .and(StringUtils.isBlank(projectName) ? null : projectNameLike(projectName))
                .and(ServiceTicketSpecification.dateRange(issuedAfter, issuedBefore))
                .and(StringUtils.isBlank(submitterFirstName) ? null : userFirstNameEquals(submitterFirstName))
                .and(StringUtils.isBlank(submitterLastName) ? null : userLastNameEquals(submitterLastName))
                .and(StringUtils.isBlank(submitterEmail) ? null : userEmailEquals(submitterEmail))
                .and(submitterId == null ? null : userIdEquals(submitterId));

        List<ServiceTicket> serviceTickets = serviceTicketRepository.findAll(filters);

        if (serviceTickets.isEmpty()) {
            throw new RecordNotFoundException("Could not find service tickets with these parameters in the database.");
        } else {
            return serviceTickets;
        }
    }

    public ServiceTicket findById(UserDetails userDetails, Long id) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket ticket = serviceTicket.get();
            User submittedBy = ticket.getSubmittedBy();

            if (hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails) || userDetails.getUsername().equals(submittedBy.getEmail())) {
                return ticket;
            } else {
                throw new InvalidRequestException("You do not have the required privileges to access this ticket.");
            }
        }
    }

    public ServiceTicket replaceServiceTicket(
            UserDetails userDetails,
            Long id,
            ServiceTicketCreationDTO newServiceTicket
    ) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();
            ServiceTicket newTicket = newServiceTicket.fromDto(projectRepository);
            User submittedBy = existingServiceTicket.getSubmittedBy();

            if (hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails) || userDetails.getUsername().equals(submittedBy.getEmail())) {
                ObjectCopyUtils.copyNonNullProperties(newTicket, existingServiceTicket);

                serviceTicketRepository.save(existingServiceTicket);

                return existingServiceTicket;
            } else {
                throw new InvalidRequestException("You do not have the required privileges to change this ticket.");
            }
        }
    }

    public String deleteServiceTicket(
            UserDetails userDetails,
            Long id
    ) {
        Optional<ServiceTicket> serviceTicket = serviceTicketRepository.findById(id);

        if (serviceTicket.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket with id '" + id + "' in database.");
        } else {
            ServiceTicket existingServiceTicket = serviceTicket.get();
            User submittedBy = existingServiceTicket.getSubmittedBy();

            if (hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails) || userDetails.getUsername().equals(submittedBy.getEmail())) {
                serviceTicketRepository.delete(existingServiceTicket);

                return "Service ticket with id '" + id + "' was successfully deleted.";
            } else {
                throw new InvalidRequestException("You do not have the required privileges to delete this ticket.");
            }
        }
    }
}

package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.configurations.websocket.TicketUpdateNotifier;
import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.projects.ProjectRepository;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.users.User;
import nl.helvar.servicetickets.users.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.UserDetailsValidator.hasPrivilege;
import static nl.helvar.servicetickets.servicetickets.ServiceTicketSpecification.*;
import static nl.helvar.servicetickets.users.UserSpecification.userEmailEquals;

@Service
public class ServiceTicketService {
    private final ServiceTicketRepository serviceTicketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final TicketUpdateNotifier ticketUpdateNotifier;

    public ServiceTicketService(
            ServiceTicketRepository serviceTicketRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            SimpMessagingTemplate messagingTemplate,
            TicketUpdateNotifier ticketUpdateNotifier
    ) {
        this.serviceTicketRepository = serviceTicketRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.ticketUpdateNotifier = ticketUpdateNotifier;
    }

    public ServiceTicketDTO createServiceTicket(UserDetails userDetails, ServiceTicketCreationDTO serviceTicketCreationDTO) {
        if (serviceTicketCreationDTO.getSource() == null) {
            serviceTicketCreationDTO.setSource("WEB");
        }

        // Build entity from DTO (project, fields, etc.)
        ServiceTicket serviceTicket = serviceTicketCreationDTO.fromDto(projectRepository);

        // Resolve who the submitter is (self or another user if allowed)
        User submittedBy = resolveSubmitter(userDetails, serviceTicketCreationDTO);
        serviceTicket.setSubmittedBy(submittedBy);

        // Persist
        serviceTicketRepository.save(serviceTicket);

        // Map to output
        return ServiceTicketDTO.toDto(serviceTicket);
    }

    public List<ServiceTicket> getAllServiceTickets(
            String type,
            String status,
            String source,
            Long projectId,
            String projectName,
            LocalDate issuedBefore,
            LocalDate issuedAfter,
            String submitterFirstName,
            String submitterLastName,
            String submitterEmail,
            Long submitterId
    ) {
        Specification<ServiceTicket> filters = Specification.where(StringUtils.isBlank(type) ? null : ticketTypeEquals(type))
                .and(StringUtils.isBlank(status) ? null : ticketStatusEquals(status))
                .and(StringUtils.isBlank(source) ? null : ticketSourceEquals(source))
                .and(projectId == null ? null : ticketProjectIdEquals(projectId))
                .and(StringUtils.isBlank(projectName) ? null : ticketProjectNameLike(projectName))
                .and(ServiceTicketSpecification.ticketDateRange(issuedAfter, issuedBefore))
                .and(StringUtils.isBlank(submitterFirstName) ? null : ticketUserFirstNameEquals(submitterFirstName))
                .and(StringUtils.isBlank(submitterLastName) ? null : ticketUserLastNameEquals(submitterLastName))
                .and(StringUtils.isBlank(submitterEmail) ? null : ticketUserEmailEquals(submitterEmail))
                .and(submitterId == null ? null : ticketUserIdEquals(submitterId));

        List<ServiceTicket> serviceTickets = serviceTicketRepository.findAll(filters);

        if (serviceTickets.isEmpty()) {
            throw new RecordNotFoundException("Could not find service tickets with these parameters in the database.");
        } else {
            return serviceTickets;
        }
    }

    public List<ServiceTicket> getAllServiceTicketsFiltered(
            UserDetails userDetails,
            String type,
            String status,
            String source,
            Long projectId,
            String projectName,
            LocalDate issuedBefore,
            LocalDate issuedAfter,
            String submitterFirstName,
            String submitterLastName,
            String submitterEmail,
            Long submitterId,
            String sortBy,
            String sortOrder,
            Integer limit
    ) {
        boolean canModerate = hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails);

        Specification<ServiceTicket> filters = Specification.where(StringUtils.isBlank(type) ? null : ticketTypeEquals(type))
                .and(StringUtils.isBlank(status) ? null : ticketStatusEquals(status))
                .and(StringUtils.isBlank(source) ? null : ticketSourceEquals(source))
                .and(projectId == null ? null : ticketProjectIdEquals(projectId))
                .and(StringUtils.isBlank(projectName) ? null : ticketProjectNameLike(projectName))
                .and(ServiceTicketSpecification.ticketDateRange(issuedAfter, issuedBefore))
                .and(StringUtils.isBlank(submitterFirstName) ? null : ticketUserFirstNameEquals(submitterFirstName))
                .and(StringUtils.isBlank(submitterLastName) ? null : ticketUserLastNameEquals(submitterLastName))
                .and(StringUtils.isBlank(submitterEmail) ? null : ticketUserEmailEquals(submitterEmail))
                .and(submitterId == null ? null : ticketUserIdEquals(submitterId));

        // Apply user restriction if not moderator
        if (!canModerate) {
            filters = filters.and(ticketUserEmailEquals(userDetails.getUsername()));
        }

        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy != null ? sortBy : "creationDate");
        Pageable pageable = limit != null ? PageRequest.of(0, limit, sort) : Pageable.unpaged();

        List<ServiceTicket> serviceTickets = serviceTicketRepository.findAll(filters, pageable).getContent();

        if (serviceTickets.isEmpty()) {
            throw new RecordNotFoundException("Could not find service tickets with these parameters in the database.");
        }

        return serviceTickets;
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

    public ServiceTicketDTO updateTicketStatus(UserDetails userDetails, Long id, TicketStatus newStatus) {
        ServiceTicket ticket = serviceTicketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Ticket with id '" + id + "' not found."));

        // Security: only moderators or ticket owner can change status
        if (!hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails) &&
                !userDetails.getUsername().equals(ticket.getSubmittedBy().getEmail())) {
            throw new InvalidRequestException("You do not have permission to change this ticket's status.");
        }

        ticket.setStatus(newStatus);
        serviceTicketRepository.save(ticket);
        serviceTicketRepository.flush();

        messagingTemplate.convertAndSend(
                "/topic/tickets",
                ServiceTicketDTO.toDto(ticket)
        );

        // Broadcast to detail view
        ticketUpdateNotifier.broadcastTicketUpdate(ServiceTicketDTO.toDto(ticket));


        return ServiceTicketDTO.toDto(ticket);
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

            if (hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails)
                    || userDetails.getUsername().equals(submittedBy.getEmail())) {

                // Prevent submittedBy changes unless moderator explicitly allowed to
                if (newServiceTicket.getSubmittedByUserId() != null
                        && !newServiceTicket.getSubmittedByUserId().equals(submittedBy.getId())) {
                    // Either ignore silently or enforce moderator-only:
                    if (!hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails)) {
                        throw new InvalidRequestException("Changing ticket submitter is not allowed.");
                    }
                    // If you DO want to allow moderators to change it:
                    User newSubmitter = userRepository.findById(newServiceTicket.getSubmittedByUserId())
                            .orElseThrow(() -> new RecordNotFoundException(
                                    "Could not find target user with id '" + newServiceTicket.getSubmittedByUserId() + "'."));
                    existingServiceTicket.setSubmittedBy(newSubmitter);
                }

                // Copy all other non-null fields
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

    // METHODS:
    private User resolveSubmitter(UserDetails userDetails, ServiceTicketCreationDTO dto) {
        // 1) Who is calling?
        User creator = userRepository.findOne(Specification.where(userEmailEquals(userDetails.getUsername())))
                .orElseThrow(() -> new RecordNotFoundException(
                        "Could not find user with email '" + userDetails.getUsername() + "' in database.")
                );

        // 2) Default submitter is the creator
        User submittedBy = creator;

        // 3) If caller tries to submit for another user, check permission and load target user
        Long requestedSubmitterId = dto.getSubmittedByUserId();
        if (requestedSubmitterId != null && !requestedSubmitterId.equals(creator.getId())) {
            boolean canCreateForOthers = hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails);

            if (!canCreateForOthers) {
                throw new InvalidRequestException("You are not allowed to create tickets for other users.");
            }

            submittedBy = userRepository.findById(requestedSubmitterId)
                    .orElseThrow(() -> new RecordNotFoundException(
                            "Could not find target user with id '" + requestedSubmitterId + "'.")
                    );
        }

        return submittedBy;
    }
}

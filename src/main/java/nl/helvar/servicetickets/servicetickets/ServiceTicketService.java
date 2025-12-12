package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.configurations.websocket.TicketUpdateNotifier;
import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.prioritization.TicketPriorityEvaluator;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
    private final TicketPriorityEvaluator ticketPriorityEvaluator;

    public ServiceTicketService(
            ServiceTicketRepository serviceTicketRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            SimpMessagingTemplate messagingTemplate,
            TicketUpdateNotifier ticketUpdateNotifier,
            TicketPriorityEvaluator ticketPriorityEvaluator
    ) {
        this.serviceTicketRepository = serviceTicketRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.ticketUpdateNotifier = ticketUpdateNotifier;
        this.ticketPriorityEvaluator = ticketPriorityEvaluator;
    }

    public ServiceTicketDTO createServiceTicket(UserDetails userDetails, ServiceTicketCreationDTO serviceTicketCreationDTO) {
        if (serviceTicketCreationDTO.getSource() == null) {
            serviceTicketCreationDTO.setSource("WEB");
        }

        // Build entity from DTO (project, fields, etc.)
        ServiceTicket serviceTicket = serviceTicketCreationDTO.fromDto(projectRepository);

        // Ensure creationDate is set before we compute any snapshot fields
        if (serviceTicket.getCreationDate() == null) {
            serviceTicket.setCreationDate(Instant.now());
        }

        // Resolve who the submitter is (self or another user if allowed)
        User submittedBy = resolveSubmitter(userDetails, serviceTicketCreationDTO);
        serviceTicket.setSubmittedBy(submittedBy);

        // Snapshot whether this ticket was created while the project had a valid contract
        applyContractSnapshotAtCreation(serviceTicket);

        serviceTicket.setTicketPriority(ticketPriorityEvaluator.evaluate(serviceTicket));
        // Persist
        serviceTicketRepository.save(serviceTicket);
        serviceTicketRepository.flush();

        // Broadcast
        ticketUpdateNotifier.broadcastTicketCreated(ServiceTicketDTO.toDto(serviceTicket));

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
            String priority,
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
                .and(StringUtils.isBlank(source) ? null : ticketPriorityEquals(priority))
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

    public ServiceTicketDTO updateServiceTicket(
            UserDetails userDetails,
            Long id,
            ServiceTicketUpdateDTO updates
    ) {
        ServiceTicket ticket = serviceTicketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Ticket with id '" + id + "' not found.")
                );

        User submittedBy = ticket.getSubmittedBy();

        if (!hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails) &&
                !userDetails.getUsername().equals(submittedBy.getEmail())) {
            throw new InvalidRequestException("You do not have permission to modify this ticket.");
        }

        // PATCH ONLY PROVIDED FIELDS:
        if (updates.getName() != null) {
            ticket.setName(updates.getName());
        }
        if (updates.getDescription() != null) {
            ticket.setDescription(updates.getDescription());
        }
        if (updates.getType() != null) {
            ticket.setType(updates.getType());
        }
        if (updates.getProjectId() != null) {
            var project = projectRepository.findById(updates.getProjectId())
                    .orElseThrow(() -> new RecordNotFoundException(
                            "Could not find project with id '" + updates.getProjectId() + "'.")
                    );
            ticket.setProject(project);
        }

        ticket.setTicketPriority(ticketPriorityEvaluator.evaluate(ticket));

        serviceTicketRepository.save(ticket);
        serviceTicketRepository.flush();

        ticketUpdateNotifier.broadcastTicketUpdate(ServiceTicketDTO.toDto(ticket));

        return ServiceTicketDTO.toDto(ticket);
    }

    public ServiceTicketDTO updateTicketStatus(UserDetails userDetails, Long id, TicketStatus newStatus) {
        ServiceTicket ticket = serviceTicketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Ticket with id '" + id + "' not found."));

        // Security: only moderators or ticket owner can change status
        if (!hasPrivilege("CAN_MODERATE_SERVICE_TICKETS_PRIVILEGE", userDetails) &&
                !userDetails.getUsername().equals(ticket.getSubmittedBy().getEmail())) {
            throw new InvalidRequestException("You do not have permission to change this ticket's status.");
        }

        if (newStatus == TicketStatus.CLOSED || newStatus == TicketStatus.CANCELLED) {
            ticket.setClosingDate(Instant.now());
        }

        ticket.setStatus(newStatus);
        ticket.setTicketPriority(ticketPriorityEvaluator.evaluate(ticket));

        serviceTicketRepository.save(ticket);
        serviceTicketRepository.flush();

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

                existingServiceTicket.setTicketPriority(ticketPriorityEvaluator.evaluate(existingServiceTicket));

                serviceTicketRepository.save(existingServiceTicket);
                serviceTicketRepository.flush();

                ticketUpdateNotifier.broadcastTicketUpdate(ServiceTicketDTO.toDto(existingServiceTicket));

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

                ticketUpdateNotifier.broadcastTicketDeleted(id);

                return "Service ticket with id '" + id + "' was successfully deleted.";
            } else {
                throw new InvalidRequestException("You do not have the required privileges to delete this ticket.");
            }
        }
    }

    // METHODS:
    private void applyContractSnapshotAtCreation(ServiceTicket ticket) {
        if (ticket == null || ticket.getProject() == null || ticket.getCreationDate() == null) {
            return;
        }

        // Convert ticket creation moment to a LocalDate in UTC (stable and predictable)
        LocalDate ticketDate = ticket.getCreationDate().atZone(ZoneOffset.UTC).toLocalDate();

        var contract = findContractCoveringDate(ticket.getProject(), ticketDate);

        if (contract == null) {
            ticket.setHadValidContractAtCreation(false);
            ticket.setContractValidFromAtCreation(null);
            ticket.setContractValidUntilAtCreation(null);
            return;
        }

        ticket.setHadValidContractAtCreation(true);

        // Persist the validity window as Instants (UTC start-of-day)
        if (contract.getStartDate() != null) {
            ticket.setContractValidFromAtCreation(contract.getStartDate().atStartOfDay(ZoneOffset.UTC).toInstant());
        } else {
            ticket.setContractValidFromAtCreation(null);
        }

        if (contract.getEndDate() != null) {
            // Inclusive end date: store as end-of-day UTC
            ticket.setContractValidUntilAtCreation(
                    contract.getEndDate().plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusMillis(1)
            );
        } else {
            ticket.setContractValidUntilAtCreation(null);
        }
    }

    private nl.helvar.servicetickets.servicecontracts.ServiceContract findContractCoveringDate(
            nl.helvar.servicetickets.projects.Project project,
            LocalDate ticketDate
    ) {
        if (project == null || ticketDate == null) {
            return null;
        }

        // This assumes Project exposes the current contract as `getServiceContract()`.
        // If your getter name differs, adjust this line accordingly.
        nl.helvar.servicetickets.servicecontracts.ServiceContract current = project.getServiceContract();

        while (current != null) {
            LocalDate start = current.getStartDate();
            LocalDate end = current.getEndDate();

            boolean hasStart = start != null;
            boolean hasEnd = end != null;

            // Treat missing start/end defensively (should ideally be non-null in DB)
            boolean startsOk = !hasStart || !ticketDate.isBefore(start);
            boolean endsOk = !hasEnd || !ticketDate.isAfter(end);

            if (startsOk && endsOk) {
                return current;
            }

            current = current.getPreviousContract();
        }

        return null;
    }

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

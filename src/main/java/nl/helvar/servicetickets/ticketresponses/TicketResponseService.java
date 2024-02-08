package nl.helvar.servicetickets.ticketresponses;

import nl.helvar.servicetickets.email.EmailService;
import nl.helvar.servicetickets.exceptions.InvalidRequestException;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.helpers.ObjectCopyUtils;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicecontracts.ServiceContractRepository;
import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.ticketresponses.subclasses.EngineerResponse;
import nl.helvar.servicetickets.users.User;
import nl.helvar.servicetickets.users.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static nl.helvar.servicetickets.helpers.UserDetailsValidator.hasPrivilege;
import static nl.helvar.servicetickets.ticketresponses.TicketResponseSpecification.responseUserIdEquals;
import static nl.helvar.servicetickets.users.UserSpecification.userEmailEquals;

@Service
public class TicketResponseService {
    private final TicketResponseRepository ticketResponseRepository;
    private final ServiceTicketRepository serviceTicketRepository;
    private final ServiceContractRepository serviceContractRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public TicketResponseService(TicketResponseRepository ticketResponseRepository,
                                 ServiceTicketRepository serviceTicketRepository,
                                 ServiceContractRepository serviceContractRepository,
                                 UserRepository userRepository,
                                 EmailService emailService
                                 ) {
        this.ticketResponseRepository = ticketResponseRepository;
        this.serviceTicketRepository = serviceTicketRepository;
        this.serviceContractRepository = serviceContractRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public TicketResponseDTO createTicketResponse(
            UserDetails userDetails,
            TicketResponseCreationDTO ticketResponseCreationDTO
    ) {
        boolean isEngineerResponse = hasPrivilege("CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE", userDetails);
        LocalDateTime currentTime = LocalDateTime.now();
        ticketResponseCreationDTO.setCreationDate(currentTime);

        if (isEngineerResponse) {
            ticketResponseCreationDTO.setIsEngineerResponse(true);
        }

        TicketResponse ticketResponse = ticketResponseCreationDTO.fromDto(serviceTicketRepository);

        Specification<User> filter = Specification.where(userEmailEquals(userDetails.getUsername()));
        Optional<User> user = userRepository.findOne(filter);

        user.ifPresent(ticketResponse::setSubmittedBy);

        if (isEngineerResponse) {

            ServiceTicket ticket = ticketResponse.getTicket();

            if(ticket.getStatus() == TicketStatus.OPEN) {
                ticket.setStatus(TicketStatus.PENDING);
            }

            ServiceContract contract = ticketResponse.getTicket().getProject().getServiceContract();

            if (contract != null) {
                int minutesSpent = ticketResponseCreationDTO.getMinutesSpent();

                contract.addUsedTime(minutesSpent);
            }
        }

        String ticketOwnerMail = ticketResponse.getTicket().getSubmittedBy().getEmail();

        try {
            emailService.sendTicketUpdate(ticketOwnerMail, ticketResponse.getTicket().getName(), ticketResponse.getResponse());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ticketResponseRepository.save(ticketResponse);

        return TicketResponseDTO.toDto(ticketResponse);
    }

    public List<TicketResponseDTO> getAllServiceTicketResponses(
            Long userId,
            String email,
            String firstName,
            String lastName
    ) {
        Specification<TicketResponse> filter = Specification.where(userId == null ? null : responseUserIdEquals(userId))
                .and(StringUtils.isBlank(email) ? null : TicketResponseSpecification.responseUserEmailEquals(email))
                .and(StringUtils.isBlank(firstName) ? null : TicketResponseSpecification.responseFirstNameEquals(firstName))
                .and(StringUtils.isBlank(lastName) ? null : TicketResponseSpecification.responseLastNameEquals(lastName));

        List<TicketResponseDTO> ticketResponses = ticketResponseRepository.findAll(filter)
                .stream()
                .map(TicketResponseDTO::toDto)
                .toList();

        if (ticketResponses.isEmpty()) {
            throw new RecordNotFoundException("Could not find any ticket responses with those parameters in the database.");
        } else {
            return ticketResponses;
        }
    }

    public TicketResponseDTO getTicketResponseById(Long id) {
        Optional<TicketResponse> ticketResponseOptional = ticketResponseRepository.findById(id);

        if (ticketResponseOptional.isEmpty()) {
            throw new RecordNotFoundException("Ticket response with id '" + id + "' was not found in the database.");
        } else {
            return TicketResponseDTO.toDto(ticketResponseOptional.get());
        }
    }

    public TicketResponseDTO replaceTicketResponse(UserDetails userDetails, Long id, TicketResponseCreationDTO newTicketResponseDTO) {
        Optional<TicketResponse> ticketResponse = ticketResponseRepository.findById(id);

        if (ticketResponse.isEmpty()) {
            throw new RecordNotFoundException("Ticket response with id '" + id + "' was not found in the database.");
        } else {
            TicketResponse existingTicketResponse = ticketResponse.get();
            TicketResponse newTicketResponse = newTicketResponseDTO.fromPutDto();
            User submittedBy = existingTicketResponse.getSubmittedBy();


            if (hasPrivilege("CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE", userDetails) || userDetails.getUsername().equals(submittedBy.getEmail())) {

                if (submittedBy.hasPrivilege("CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE")) {
                    ServiceContract contract = existingTicketResponse.getTicket().getProject().getServiceContract();

                    if (contract != null) {
                        int oldMinutesSpent = ((EngineerResponse) existingTicketResponse).getMinutesSpent();
                        int newMinutesSpent = ((EngineerResponse) newTicketResponse).getMinutesSpent();
                        int valueAdjustment = newMinutesSpent - oldMinutesSpent;

                        contract.addUsedTime(valueAdjustment);
                        serviceContractRepository.save(contract);
                    }
                } else {
                    if (newTicketResponseDTO.getMinutesSpent() != 0) {
                        throw new InvalidRequestException("You cannot add minutes spent to a non-engineer response.");
                    }
                }

                if (existingTicketResponse instanceof EngineerResponse engineerResponse) {
                    ObjectCopyUtils.copyNonNullProperties(newTicketResponse, engineerResponse);

                    ticketResponseRepository.save(engineerResponse);

                    return TicketResponseDTO.toDto(engineerResponse);
                } else {
                    ObjectCopyUtils.copyNonNullProperties(newTicketResponse, existingTicketResponse);

                    ticketResponseRepository.save(existingTicketResponse);

                    return TicketResponseDTO.toDto(existingTicketResponse);
                }
            } else {
                throw new InvalidRequestException("You do not have the required privileges to change this ticket response.");
            }
        }
    }

    public String deleteTicketResponse(UserDetails userDetails, Long id) {
        Optional<TicketResponse> ticketResponse = ticketResponseRepository.findById(id);

        if (ticketResponse.isEmpty()) {
            throw new RecordNotFoundException("Ticket response with id '" + id + "' was not found in the database.");
        } else {
            User submittedBy = ticketResponse.get().getSubmittedBy();

            if (hasPrivilege("CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE", userDetails) || userDetails.getUsername().equals(submittedBy.getEmail())) {
                ticketResponseRepository.deleteById(id);

                return "Ticket response with id '" + id + "' was successfully deleted.";
            } else {
                throw new InvalidRequestException("You do not have the required privileges to delete this ticket response.");
            }
        }
    }
}

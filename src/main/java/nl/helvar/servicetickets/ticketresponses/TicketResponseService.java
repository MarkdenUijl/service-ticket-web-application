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
import static nl.helvar.servicetickets.projects.ProjectSpecification.houseNumberLike;
import static nl.helvar.servicetickets.ticketresponses.TicketResponseSpecification.userIdEquals;
import static nl.helvar.servicetickets.users.UserSpecification.emailEquals;

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
        LocalDateTime currentTime = LocalDateTime.now();
        ticketResponseCreationDTO.setCreationDate(currentTime);

        TicketResponse ticketResponse = ticketResponseCreationDTO.fromDto(serviceTicketRepository);

        Specification<User> filter = Specification.where(emailEquals(userDetails.getUsername()));
        Optional<User> user = userRepository.findOne(filter);

        user.ifPresent(ticketResponse::setSubmittedBy);

        if (hasPrivilege("CAN_MAKE_ENGINEER_RESPONSE_PRIVILEGE", userDetails)) {
            Optional<ServiceTicket> optionalTicket = serviceTicketRepository.findById(ticketResponseCreationDTO.getServiceTicketId());

            if (optionalTicket.isPresent()) {
                ServiceTicket ticket = optionalTicket.get();

                if(ticket.getStatus() == TicketStatus.OPEN) {
                    ticket.setStatus(TicketStatus.PENDING);
                    serviceTicketRepository.save(ticket);
                }
            }

            Optional<ServiceContract> contract = extractServiceContract(ticketResponse);

            if (contract.isPresent()) {
                int minutesSpent = ticketResponseCreationDTO.getMinutesSpent();
                ServiceContract existingContract = contract.get();

                existingContract.addUsedTime(minutesSpent);
                serviceContractRepository.save(existingContract);
            }
        }

        String ticketOwnerMail = ticketResponse.getTicket().getSubmittedBy().getEmail();

        try {
            emailService.sendTicketUpdate(ticketOwnerMail, ticketResponse.getTicket().getName(), ticketResponse.getResponse());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        if (ticketResponse instanceof EngineerResponse) {
//            Optional<ServiceTicket> ticketOptional = serviceTicketRepository.findById(ticketResponseCreationDTO.getServiceTicketId());
//
//            if (ticketOptional.isPresent()) {
//                ServiceTicket ticket = ticketOptional.get();
//                List<TicketResponse> allResponses = ticket.getResponses();
//
//                boolean hasEngineerResponse = checkListForProperty(allResponses, "minutesSpent");
//
//                if(!hasEngineerResponse) {
//                    ticket.setStatus(TicketStatus.PENDING);
//                    serviceTicketRepository.save(ticket);
//                }
//            }
//        }

        ticketResponseRepository.save(ticketResponse);

        return TicketResponseDTO.toDto(ticketResponse);
    }

    public List<TicketResponseDTO> getAllServiceTicketResponses(
            Long userId,
            String email,
            String firstName,
            String lastName
    ) {
        Specification<TicketResponse> filter = Specification.where(userId == null ? null : userIdEquals(userId))
                .and(StringUtils.isBlank(email) ? null : TicketResponseSpecification.userEmailEquals(email))
                .and(StringUtils.isBlank(firstName) ? null : TicketResponseSpecification.firstNameEquals(firstName))
                .and(StringUtils.isBlank(lastName) ? null : TicketResponseSpecification.lastNameEquals(lastName));

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
            TicketResponse newTicketResponse = newTicketResponseDTO.fromPutDto(serviceTicketRepository);
            User submittedBy = existingTicketResponse.getSubmittedBy();

            if (hasPrivilege("CAN_MODERATE_TICKET_RESPONSES_PRIVILEGE", userDetails) || userDetails.getUsername().equals(submittedBy.getEmail())) {

                if (existingTicketResponse instanceof EngineerResponse existingEngineerResponse &&
                        newTicketResponse instanceof EngineerResponse newEngineerResponse
                ) {
                    ServiceContract contract = existingEngineerResponse.getTicket().getProject().getServiceContract();

                    if (contract != null) {
                        int oldMinutesSpent = existingEngineerResponse.getMinutesSpent();
                        int newMinutesSpent = newEngineerResponse.getMinutesSpent();
                        int valueAdjustment = newMinutesSpent - oldMinutesSpent;;

                        contract.addUsedTime(valueAdjustment);
                        serviceContractRepository.save(contract);
                    }
                } else {
                    if (newTicketResponseDTO.getMinutesSpent() != 0) {
                        throw new InvalidRequestException("You cannot add minutes spent to a non-engineer response.");
                    }
                }

                ObjectCopyUtils.copyNonNullProperties(newTicketResponse, existingTicketResponse);

                ticketResponseRepository.save(existingTicketResponse);

                return TicketResponseDTO.toDto(existingTicketResponse);
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

    public Optional<ServiceContract> extractServiceContract(TicketResponse ticketResponse) {
        return serviceContractRepository.findById(ticketResponse
                .getTicket()
                .getProject()
                .getServiceContract()
                .getId()
        );
    }
}

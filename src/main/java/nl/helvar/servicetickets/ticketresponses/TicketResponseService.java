package nl.helvar.servicetickets.ticketresponses;

import nl.helvar.servicetickets.email.EmailService;
import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import nl.helvar.servicetickets.servicecontracts.ServiceContract;
import nl.helvar.servicetickets.servicecontracts.ServiceContractRepository;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;
import nl.helvar.servicetickets.ticketresponses.subclasses.EngineerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketResponseService {
    private final TicketResponseRepository ticketResponseRepository;
    private final ServiceTicketRepository serviceTicketRepository;
    private final ServiceContractRepository serviceContractRepository;
    private final EmailService emailService;

    public TicketResponseService(TicketResponseRepository ticketResponseRepository,
                                 ServiceTicketRepository serviceTicketRepository,
                                 ServiceContractRepository serviceContractRepository,
                                 EmailService emailService
            ) {
        this.ticketResponseRepository = ticketResponseRepository;
        this.serviceTicketRepository = serviceTicketRepository;
        this.serviceContractRepository = serviceContractRepository;
        this.emailService = emailService;
    }

    public TicketResponseCreationDTO createTicketResponse(TicketResponseCreationDTO ticketResponseCreationDTO) {
        LocalDateTime currentTime = LocalDateTime.now();
        ticketResponseCreationDTO.setCreationDate(currentTime);

        TicketResponse ticketResponse = ticketResponseCreationDTO.fromDto(serviceTicketRepository);

        //USER MAIL TOEVOEGEN
        try {
            emailService.sendTicketUpdate("markdenuyl@gmail.com", ticketResponse.getTicket().getName(), ticketResponse.getResponse());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Optional<ServiceContract> contract = extractServiceContract(ticketResponse);

        if (contract.isPresent()) {
            int minutesSpent = ticketResponseCreationDTO.getMinutesSpent();
            ServiceContract existingContract = contract.get();

            existingContract.addUsedTime(minutesSpent);
            serviceContractRepository.save(existingContract);
        }

        ticketResponseRepository.save(ticketResponse);

        ticketResponseCreationDTO.setId(ticketResponse.getId());
        return ticketResponseCreationDTO;
    }

    // LATER FILTER BY USER TOEPASSEN
    public List<TicketResponseDTO> getAllServiceTickets() {
        List<TicketResponseDTO> ticketResponses = ticketResponseRepository.findAll()
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

    public TicketResponseDTO replaceTicketResponse(Long id, TicketResponseCreationDTO newTicketResponse) {
        Optional<TicketResponse> ticketResponse = ticketResponseRepository.findById(id);

        if (ticketResponse.isEmpty()) {
            throw new RecordNotFoundException("Ticket response with id '" + id + "' was not found in the database.");
        } else {
            TicketResponse existingTicketResponse = ticketResponse.get();
            TicketResponse newResponse = newTicketResponse.fromDto(serviceTicketRepository);

            if (existingTicketResponse instanceof EngineerResponse existingEngineerResponse &&
                newResponse instanceof EngineerResponse newEngineerResponse
            ) {
                Optional<ServiceContract> contract = extractServiceContract(newResponse);

                if (contract.isPresent()) {
                    int oldMinutesSpent = existingEngineerResponse.getMinutesSpent();
                    int newMinutesSpent = newEngineerResponse.getMinutesSpent();
                    int valueAdjustment = newMinutesSpent - oldMinutesSpent;

                    ServiceContract existingContract = contract.get();

                    existingContract.addUsedTime(valueAdjustment);
                    serviceContractRepository.save(existingContract);
                }
            }

            BeanUtils.copyProperties(newResponse, existingTicketResponse, "id", "creationDate");

            ticketResponseRepository.save(existingTicketResponse);

            return TicketResponseDTO.toDto(existingTicketResponse);
        }
    }

    public String deleteTicketResponse(Long id) {
        Optional<TicketResponse> ticketResponse = ticketResponseRepository.findById(id);

        if (ticketResponse.isEmpty()) {
            throw new RecordNotFoundException("Ticket response with id '" + id + "' was not found in the database.");
        } else {
            ticketResponseRepository.deleteById(id);

            return "Ticket response with id '" + id + "' was successfully deleted.";
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

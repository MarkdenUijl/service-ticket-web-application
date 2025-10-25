package nl.helvar.servicetickets.prioritization;

import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.enums.TicketPriority;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

@Component
public class TicketPriorityEvaluator {

    private final PriorityRulesProvider rulesProvider;

    public TicketPriorityEvaluator(PriorityRulesProvider rulesProvider) {
        this.rulesProvider = rulesProvider;
    }

    public TicketPriority evaluate(ServiceTicket ticket) {
        if (ticket == null || ticket.getCreationDate() == null) return TicketPriority.MEDIUM;

        long ageMinutes = Duration.between(ticket.getCreationDate(), Instant.now()).toMinutes();
        boolean isContractValid = ticket.getProject() != null &&
                ticket.getProject().getServiceContract() != null &&
                ticket.getProject().getServiceContract().getEndDate() != null &&
                !ticket.getProject().getServiceContract().getEndDate().isBefore(LocalDate.now());

        if (isContractValid) {
            if (ageMinutes >= rulesProvider.ageContractCriticalThresholdMinutes()) {
                return TicketPriority.CRITICAL;
            } else {
                return TicketPriority.HIGH;
            }
        } else {
            TicketType type = ticket.getType();
            if (type == TicketType.QUESTION || type == TicketType.CHANGE) {
                return TicketPriority.LOW;
            } else {
                if (ageMinutes >= rulesProvider.ageCriticalThresholdMinutes()) {
                    return TicketPriority.CRITICAL;
                } else if (ageMinutes >= rulesProvider.ageHighThresholdMinutes()) {
                    return TicketPriority.HIGH;
                } else {
                    return TicketPriority.MEDIUM;
                }
            }
        }
    }
}
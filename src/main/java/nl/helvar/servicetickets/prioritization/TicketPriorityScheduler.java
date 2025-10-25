package nl.helvar.servicetickets.prioritization;

import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.ServiceTicketDTO;
import nl.helvar.servicetickets.servicetickets.ServiceTicketRepository;
import nl.helvar.servicetickets.servicetickets.ServiceTicketSpecification;
import nl.helvar.servicetickets.servicetickets.enums.TicketPriority;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.configurations.websocket.TicketUpdateNotifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TicketPriorityScheduler {

    private final ServiceTicketRepository repository;
    private final TicketPriorityEvaluator evaluator;
    private final TicketPriorityProperties props;
    private final TicketUpdateNotifier ticketUpdateNotifier;

    public TicketPriorityScheduler(ServiceTicketRepository repository,
                                   TicketPriorityEvaluator evaluator,
                                   TicketPriorityProperties props,
                                   @Lazy TicketUpdateNotifier ticketUpdateNotifier) {
        this.repository = repository;
        this.evaluator = evaluator;
        this.props = props;
        this.ticketUpdateNotifier = ticketUpdateNotifier;
    }

    @Scheduled(fixedRateString = "#{@ticketPriorityProperties.schedulerFixedRateSeconds * 1000}")
    @Transactional
    public void reevaluateTicketPriorities() {
        if (!props.isEnableScheduler()) return;

        int page = 0;
        int size = Math.max(1, props.getSchedulerBatchSize());

        Page<ServiceTicket> batch;

        do {
            batch = repository.findAll(
                    ServiceTicketSpecification.ticketStatusNotEquals("CLOSED"),
                    PageRequest.of(page, size)
            );

            for (ServiceTicket ticket : batch) {
                TicketPriority oldPriority = ticket.getPriority();
                TicketPriority newPriority = evaluator.evaluate(ticket);

                if (newPriority != oldPriority) {
                    ticket.setTicketPriority(newPriority);
                    repository.save(ticket);

                    // Notify WebSocket subscribers
                    ServiceTicketDTO dto = ServiceTicketDTO.toDto(ticket);
                    ticketUpdateNotifier.broadcastTicketUpdate(dto);
                }
            }

            page++;
        } while (!batch.isEmpty() && batch.hasNext());
    }
}
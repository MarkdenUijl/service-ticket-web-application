package nl.helvar.servicetickets.configurations.websocket;

import nl.helvar.servicetickets.servicetickets.ServiceTicketDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TicketUpdateNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public TicketUpdateNotifier(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Broadcast an update of a specific ticket to its own topic,
     * and also to the general /topic/tickets feed for dashboard or lists.
     */
    public void broadcastTicketUpdate(ServiceTicketDTO ticket) {
        // Individual ticket topic (detail view)
        messagingTemplate.convertAndSend("/topic/tickets/" + ticket.getId(), ticket);

        // General ticket feed (list updates)
        Map<String, Object> event = Map.of(
                "type", "UPDATED",
                "ticket", ticket,
                "ticketId", ticket.getId()
        );
        messagingTemplate.convertAndSend("/topic/tickets", event);
    }

    /**
     * Broadcast a new ticket creation.
     */
    public void broadcastTicketCreated(ServiceTicketDTO ticket) {
        messagingTemplate.convertAndSend("/topic/tickets/" + ticket.getId(), ticket);

        Map<String, Object> event = Map.of(
                "type", "CREATED",
                "ticket", ticket,
                "ticketId", ticket.getId()
        );
        messagingTemplate.convertAndSend("/topic/tickets", event);
    }

    /**
     * Broadcast a ticket deletion.
     */
    public void broadcastTicketDeleted(Long id) {
        Map<String, Object> event = Map.of(
                "type", "DELETED",
                "ticketId", id
        );
        messagingTemplate.convertAndSend("/topic/tickets", event);
    }
}
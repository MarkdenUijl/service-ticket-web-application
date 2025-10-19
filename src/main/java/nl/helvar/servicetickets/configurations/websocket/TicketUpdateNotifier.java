package nl.helvar.servicetickets.configurations.websocket;

import nl.helvar.servicetickets.servicetickets.ServiceTicketDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketUpdateNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public TicketUpdateNotifier(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastTicketUpdate(ServiceTicketDTO ticket) {
        messagingTemplate.convertAndSend("/topic/tickets/" + ticket.getId(), ticket);
    }
}
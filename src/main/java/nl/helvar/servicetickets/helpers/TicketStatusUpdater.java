package nl.helvar.servicetickets.helpers;

import nl.helvar.servicetickets.servicetickets.ServiceTicket;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;

public class TicketStatusUpdater {
    public static void updateTicketStatusAutomatically(ServiceTicket ticket, boolean isEngineerResponse) {
        if (ticket == null) return;

        TicketStatus currentStatus = ticket.getStatus();

        // Don't update if the ticket is closed or escalated
        if (currentStatus == TicketStatus.CLOSED || currentStatus == TicketStatus.ESCALATED) {
            return;
        }

        if (isEngineerResponse) {
            // Engineer responded
            if (currentStatus == TicketStatus.OPEN || currentStatus == TicketStatus.IN_PROGRESS) {
                ticket.setStatus(TicketStatus.PENDING);
            }
        } else {
            // Customer responded
            if (currentStatus == TicketStatus.PENDING) {
                ticket.setStatus(TicketStatus.IN_PROGRESS);
            }
        }
    }
}

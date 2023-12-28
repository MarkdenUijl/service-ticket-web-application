package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.exceptions.InvalidEnumConstantException;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import org.springframework.data.jpa.domain.Specification;

public class ServiceTicketSpecification {
    private ServiceTicketSpecification() {};

    // First request parameter filter: Service ticket type
    public static Specification<ServiceTicket> typeEquals(String ticketType) {
        try {
            TicketType type = TicketType.valueOf(ticketType.toUpperCase());
            return (root, query, builder) -> builder.equal(root.get("type"), type);
        } catch (IllegalArgumentException ex) {
            throw new InvalidEnumConstantException(TicketType.values());
        }
    }

    // Second request parameter filter: Service ticket status
    public static Specification<ServiceTicket> statusEquals(String ticketStatus) {
        try {
            TicketStatus status = TicketStatus.valueOf(ticketStatus.toUpperCase());
            return (root, query, builder) -> builder.equal(root.get("status"), status);
        } catch (IllegalArgumentException ex) {
            throw new InvalidEnumConstantException(TicketStatus.values());
        }
    }

}

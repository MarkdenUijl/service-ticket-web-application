package nl.helvar.servicetickets.servicetickets;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import nl.helvar.servicetickets.exceptions.InvalidEnumConstantException;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // Third request parameter filter: Service ticket project id
    public static Specification<ServiceTicket> projectEquals(Long projectId) {
        return ((root, query, builder) -> {
            Join<ServiceTicket, Project> projectJoin = root.join("project");
            Predicate predicate = builder.equal(projectJoin.get("id"), projectId);

            return predicate;
        });
    }

    // Fourth request parameter filter: issued after date
    public static Specification<ServiceTicket> issuedAfterDate(LocalDate issuedAfter) {
        return (root, query, builder) -> {
            if (issuedAfter != null) {
                LocalDateTime issuedAfterDateTime = issuedAfter.atStartOfDay();
                return builder.greaterThanOrEqualTo(root.get("creationDate"), issuedAfterDateTime);
            }
            return null;
        };
    }

    // Fifth request parameter filter: issued before date
    public static Specification<ServiceTicket> issuedBeforeDate(LocalDate issuedBefore) {
        return (root, query, builder) -> {
            if (issuedBefore != null) {
                LocalDateTime issuedBeforeDateTime = issuedBefore.atStartOfDay().plusDays(1);
                return builder.lessThan(root.get("creationDate"), issuedBeforeDateTime);
            }
            return null;
        };
    }

    // Combined request parameters fourth and fifth
    public static Specification<ServiceTicket> dateRange(LocalDate issuedAfter, LocalDate issuedBefore) {
        return Specification.where(issuedAfterDate(issuedAfter)).and(issuedBeforeDate(issuedBefore));
    }
}

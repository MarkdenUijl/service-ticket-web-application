package nl.helvar.servicetickets.servicetickets;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import nl.helvar.servicetickets.exceptions.InvalidEnumConstantException;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.servicetickets.enums.TicketStatus;
import nl.helvar.servicetickets.servicetickets.enums.TicketType;
import nl.helvar.servicetickets.ticketresponses.TicketResponse;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ServiceTicketSpecification {
    public static Specification<ServiceTicket> typeEquals(String ticketType) {
        try {
            TicketType type = TicketType.valueOf(ticketType.toUpperCase());
            return (root, query, builder) -> builder.equal(root.get("type"), type);
        } catch (IllegalArgumentException ex) {
            throw new InvalidEnumConstantException(TicketType.values());
        }
    }

    public static Specification<ServiceTicket> statusEquals(String ticketStatus) {
        try {
            TicketStatus status = TicketStatus.valueOf(ticketStatus.toUpperCase());
            return (root, query, builder) -> builder.equal(root.get("status"), status);
        } catch (IllegalArgumentException ex) {
            throw new InvalidEnumConstantException(TicketStatus.values());
        }
    }

    public static Specification<ServiceTicket> projectIdEquals(Long projectId) {
        return ((root, query, builder) -> {
            Join<ServiceTicket, Project> projectJoin = root.join("project");
            Predicate predicate = builder.equal(projectJoin.get("id"), projectId);

            return predicate;
        });
    }

    public static Specification<ServiceTicket> projectNameLike(String projectName) {
        String formattedNameLike = "%" + projectName.toLowerCase() + "%";
        return ((root, query, builder) -> {
            Join<ServiceTicket, Project> projectJoin = root.join("project");

            return builder.like(builder.lower(projectJoin.get("name")), formattedNameLike);
        });
    }

    public static Specification<ServiceTicket> issuedAfterDate(LocalDate issuedAfter) {
        return (root, query, builder) -> {
            if (issuedAfter != null) {
                LocalDateTime issuedAfterDateTime = issuedAfter.atStartOfDay();
                return builder.greaterThanOrEqualTo(root.get("creationDate"), issuedAfterDateTime);
            }
            return null;
        };
    }

    public static Specification<ServiceTicket> issuedBeforeDate(LocalDate issuedBefore) {
        return (root, query, builder) -> {
            if (issuedBefore != null) {
                LocalDateTime issuedBeforeDateTime = issuedBefore.atStartOfDay().plusDays(1);
                return builder.lessThan(root.get("creationDate"), issuedBeforeDateTime);
            }
            return null;
        };
    }

    public static Specification<ServiceTicket> dateRange(LocalDate issuedAfter, LocalDate issuedBefore) {
        return Specification.where(issuedAfterDate(issuedAfter)).and(issuedBeforeDate(issuedBefore));
    }

    public static Specification<ServiceTicket> userIdEquals(Long userId) {
        return (root, query, builder) -> builder.equal(root.get("submittedBy").get("id"), userId);
    }

    public static Specification<ServiceTicket> userEmailEquals(String email) {
        return (root, query, builder) -> builder.equal(root.get("submittedBy").get("email"), email);
    }

    public static Specification<ServiceTicket> userFirstNameEquals(String firstName) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("submittedBy").get("firstName")), firstName.toLowerCase());
    }

    public static Specification<ServiceTicket> userLastNameEquals(String lastName) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("submittedBy").get("lastName")), lastName.toLowerCase());
    }
}

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
    public static Specification<ServiceTicket> ticketTypeEquals(String ticketType) {
        try {
            TicketType type = TicketType.valueOf(ticketType.toUpperCase());
            return (root, query, builder) -> builder.equal(root.get("type"), type);
        } catch (IllegalArgumentException ex) {
            throw new InvalidEnumConstantException(TicketType.values());
        }
    }

    public static Specification<ServiceTicket> ticketStatusEquals(String ticketStatus) {
        try {
            TicketStatus status = TicketStatus.valueOf(ticketStatus.toUpperCase());
            return (root, query, builder) -> builder.equal(root.get("status"), status);
        } catch (IllegalArgumentException ex) {
            throw new InvalidEnumConstantException(TicketStatus.values());
        }
    }

    public static Specification<ServiceTicket> ticketProjectIdEquals(Long projectId) {
        return ((root, query, builder) -> {
            Join<ServiceTicket, Project> projectJoin = root.join("project");
            Predicate predicate = builder.equal(projectJoin.get("id"), projectId);

            return predicate;
        });
    }

    public static Specification<ServiceTicket> ticketProjectNameLike(String projectName) {
        String formattedNameLike = "%" + projectName.toLowerCase() + "%";
        return ((root, query, builder) -> {
            Join<ServiceTicket, Project> projectJoin = root.join("project");

            return builder.like(builder.lower(projectJoin.get("name")), formattedNameLike);
        });
    }

    public static Specification<ServiceTicket> ticketIssuedAfterDate(LocalDate issuedAfter) {
        return (root, query, builder) -> {
            if (issuedAfter != null) {
                LocalDateTime issuedAfterDateTime = issuedAfter.atStartOfDay();
                return builder.greaterThanOrEqualTo(root.get("creationDate"), issuedAfterDateTime);
            }
            return null;
        };
    }

    public static Specification<ServiceTicket> ticketIssuedBeforeDate(LocalDate issuedBefore) {
        return (root, query, builder) -> {
            if (issuedBefore != null) {
                LocalDateTime issuedBeforeDateTime = issuedBefore.atStartOfDay().plusDays(1);
                return builder.lessThan(root.get("creationDate"), issuedBeforeDateTime);
            }
            return null;
        };
    }

    public static Specification<ServiceTicket> ticketDateRange(LocalDate issuedAfter, LocalDate issuedBefore) {
        return Specification.where(ticketIssuedAfterDate(issuedAfter)).and(ticketIssuedBeforeDate(issuedBefore));
    }

    public static Specification<ServiceTicket> ticketUserIdEquals(Long userId) {
        return (root, query, builder) -> builder.equal(root.get("submittedBy").get("id"), userId);
    }

    public static Specification<ServiceTicket> ticketUserEmailEquals(String email) {
        return (root, query, builder) -> builder.equal(root.get("submittedBy").get("email"), email);
    }

    public static Specification<ServiceTicket> ticketUserFirstNameEquals(String firstName) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("submittedBy").get("firstName")), firstName.toLowerCase());
    }

    public static Specification<ServiceTicket> ticketUserLastNameEquals(String lastName) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("submittedBy").get("lastName")), lastName.toLowerCase());
    }
}

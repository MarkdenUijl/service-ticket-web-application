package nl.helvar.servicetickets.ticketresponses;

import org.springframework.data.jpa.domain.Specification;

public class TicketResponseSpecification {
    private TicketResponseSpecification() {}

    public static Specification<TicketResponse> userIdEquals(Long userId) {
        return (root, query, builder) -> builder.equal(root.get("submittedBy").get("id"), userId);
    }

    public static Specification<TicketResponse> userEmailEquals(String email) {
        return (root, query, builder) -> builder.equal(root.get("submittedBy").get("email"), email);
    }

    public static Specification<TicketResponse> firstNameEquals(String firstName) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("submittedBy").get("firstName")), firstName.toLowerCase());
    }

    public static Specification<TicketResponse> lastNameEquals(String lastName) {
        return (root, query, builder) -> builder.equal(builder.lower(root.get("submittedBy").get("lastName")), lastName.toLowerCase());
    }
}

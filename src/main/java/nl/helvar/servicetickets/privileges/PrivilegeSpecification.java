package nl.helvar.servicetickets.privileges;

import org.springframework.data.jpa.domain.Specification;

public class PrivilegeSpecification {
    private PrivilegeSpecification() {}

    // First request parameter filter: Find Privilege by name
    public static Specification<Privilege> privilegeEquals(String privilegeName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), privilegeName);
    }
}

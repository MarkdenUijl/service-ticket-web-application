package nl.helvar.servicetickets.roles;

import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {
    private RoleSpecification() {}

    // First request parameter filter: Find Role by name
    public static Specification<Role> roleNameEquals(String roleName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), roleName);
    }
}

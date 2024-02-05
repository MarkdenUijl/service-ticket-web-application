package nl.helvar.servicetickets.roles;

import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {
    public static Specification<Role> roleNameEquals(String roleName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), roleName);
    }
}

package nl.helvar.servicetickets.privileges;

import org.springframework.data.jpa.domain.Specification;

public class PrivilegeSpecification {
    public static Specification<Privilege> privilegeNameEquals(String privilegeName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), privilegeName);
    }
}

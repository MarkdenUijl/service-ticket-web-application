package nl.helvar.servicetickets.users;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import nl.helvar.servicetickets.roles.Role;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> roleEquals(String roleName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<User, Role> roleJoin = root.join("roles");

            return criteriaBuilder.equal(roleJoin.get("name"), roleName);
        };
    }

    public static Specification<User> roleLike(String roleName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<User, Role> roleJoin = root.join("roles");

            String lowercaseRoleName = roleName.toLowerCase();

            return criteriaBuilder.like(criteriaBuilder.lower(roleJoin.get("name")), "%" + lowercaseRoleName + "%");
        };
    }

    public static Specification<User> emailEquals(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }
}

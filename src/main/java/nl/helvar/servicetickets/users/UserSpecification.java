package nl.helvar.servicetickets.users;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import nl.helvar.servicetickets.projects.Project;
import nl.helvar.servicetickets.roles.Role;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> userRoleEquals(String roleName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<User, Role> roleJoin = root.join("roles");

            return criteriaBuilder.equal(roleJoin.get("name"), roleName);
        };
    }

    public static Specification<User> userRoleLike(String roleName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Join<User, Role> roleJoin = root.join("roles");

            String lowercaseRoleName = roleName.toLowerCase();

            return criteriaBuilder.like(criteriaBuilder.lower(roleJoin.get("name")), "%" + lowercaseRoleName + "%");
        };
    }

    public static Specification<User> userFirstNameLike(String firstNameLike) {
        String formattedFirstNameLike = "%" + firstNameLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("firstName")), formattedFirstNameLike));
    }

    public static Specification<User> userLastNameLike(String lastNameLike) {
        String formattedLastNameLike = "%" + lastNameLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("lastName")), formattedLastNameLike));
    }

    public static Specification<User> userEmailEquals(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }
}

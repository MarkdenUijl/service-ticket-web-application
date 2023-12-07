package nl.helvar.servicetickets.projects;

import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {
    private ProjectSpecification() {}

    // First request parameter filter: Project name
    public static Specification<Project> nameLike(String nameLike) {
        return ((root, query, builder) -> builder.like(root.get("name"), "%" + nameLike + "%"));
    }

    // Second request parameter filter: Project address
    public static Specification<Project> addressLike(String addressLike) {
        return ((root, query, builder) -> builder.like(root.get("name"), "%" + addressLike + "%"));
    }
}

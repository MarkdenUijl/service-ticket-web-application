package nl.helvar.servicetickets.projects;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {
    private ProjectSpecification() {}

    // First request parameter filter: Project name
    public static Specification<Project> nameLike(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("name")), formattedNameLike));
    }

    // Second request parameter filter: Project city
    public static Specification<Project> cityLike(String cityLike) {
        String formattedCityLike = "%" + cityLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("city")), formattedCityLike));
    }

    // Third request parameter filter: Project zipcode
    public static Specification<Project> zipCodeLike(String zipCodeLike) {
        String formattedZipCodeLike = "%" + zipCodeLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("zipCode")), formattedZipCodeLike));
    }

    // Fourth request parameter filter: Project street
    public static Specification<Project> streetLike(String streetLike) {
        String formattedStreetLike = "%" + streetLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("street")), formattedStreetLike));
    }

    // Fifth request parameter filter: Project house number
    public static Specification<Project> houseNumberLike(int houseNumberLike) {
        return ((root, query, builder) -> builder.equal(root.get("houseNumber"), houseNumberLike));
    }

    // Sixth request parameter filter: Project has service contract
    public static Specification<Project> serviceContractLike(boolean serviceContract) {
        return (root, query, builder) -> {
            if (serviceContract) {
                return builder.isNotNull(root.get("serviceContract"));
            } else {
                return builder.isNull(root.get("serviceContract"));
            }
        };
    }

    // Specific search specifications:
    public static Specification<Project> findByAddress(String city, String street, String zipCode, int houseNumber) {
        return (root, query, builder) -> {
            Predicate predicateByAddress = builder.and(
                    builder.equal(root.get("city"), city),
                    builder.equal(root.get("street"), street),
                    builder.equal(root.get("houseNumber"), houseNumber)
            );

            Predicate predicateByZipAndHouseNumber = builder.and(
                    builder.equal(root.get("zipCode"), zipCode),
                    builder.equal(root.get("houseNumber"), houseNumber)
            );

            return builder.or(predicateByAddress, predicateByZipAndHouseNumber);
        };
    }
}

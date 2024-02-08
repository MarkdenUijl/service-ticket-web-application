package nl.helvar.servicetickets.projects;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {
    public static Specification<Project> projectNameLike(String nameLike) {
        String formattedNameLike = "%" + nameLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("name")), formattedNameLike));
    }

    public static Specification<Project> projectCityLike(String cityLike) {
        String formattedCityLike = "%" + cityLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("city")), formattedCityLike));
    }

    public static Specification<Project> projectZipCodeLike(String zipCodeLike) {
        String formattedZipCodeLike = "%" + zipCodeLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("zipCode")), formattedZipCodeLike));
    }

    public static Specification<Project> projectStreetLike(String streetLike) {
        String formattedStreetLike = "%" + streetLike.toLowerCase() + "%";
        return ((root, query, builder) -> builder.like(builder.lower(root.get("street")), formattedStreetLike));
    }

    public static Specification<Project> projectHouseNumberLike(int houseNumberLike) {
        return ((root, query, builder) -> builder.equal(root.get("houseNumber"), houseNumberLike));
    }

    public static Specification<Project> projectServiceContractLike(boolean serviceContract) {
        return (root, query, builder) -> {
            if (serviceContract) {
                return builder.isNotNull(root.get("serviceContract"));
            } else {
                return builder.isNull(root.get("serviceContract"));
            }
        };
    }

    public static Specification<Project> findProjectByAddress(String city, String street, String zipCode, int houseNumber) {
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

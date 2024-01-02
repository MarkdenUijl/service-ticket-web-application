package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.exceptions.InvalidEnumConstantException;
import nl.helvar.servicetickets.servicecontracts.enums.ContractType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ServiceContractSpecification {
    private ServiceContractSpecification() {}

    // First request parameter filter: Service contract type
    public static Specification<ServiceContract> typeEquals(String contractType) {
        List<ContractType> matchingTypes = new ArrayList<>();

        for (ContractType type : ContractType.values()) {
            if (type.name().contains(contractType.toUpperCase())) {
                matchingTypes.add(type);
            }
        }

        if (matchingTypes.isEmpty()) {
            throw new InvalidEnumConstantException(ContractType.values());
        }

        return ((root, query, builder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            for (ContractType type : matchingTypes) {
                predicates.add(builder.equal(root.get("type"), type));
            }
            return builder.or(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });
    }
}

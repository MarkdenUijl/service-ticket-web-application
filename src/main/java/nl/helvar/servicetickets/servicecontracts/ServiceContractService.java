package nl.helvar.servicetickets.servicecontracts;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static nl.helvar.servicetickets.servicecontracts.ServiceContractSpecification.*;

@Service
public class ServiceContractService {
    private final ServiceContractRepository serviceContractRepository;

    public ServiceContractService(ServiceContractRepository serviceContractRepository) {
        this.serviceContractRepository = serviceContractRepository;
    }

    public ServiceContractCreationDTO createServiceContract(ServiceContractCreationDTO serviceContractCreationDTO) {
        ServiceContract serviceContract = serviceContractCreationDTO.fromDto();

        serviceContractRepository.save(serviceContract);

        serviceContractCreationDTO.setId(serviceContract.getId());
        return serviceContractCreationDTO;
    }

    public List<ServiceContractDTO> getAllServiceContracts(String type) {
        Specification<ServiceContract> filters = null;
        if (!StringUtils.isBlank(type)) {
            filters = typeEquals(type);
        }

        List<ServiceContractDTO> serviceContracts = serviceContractRepository.findAll(filters)
                .stream()
                .map(ServiceContractDTO::toDto)
                .toList();

        if (serviceContracts.isEmpty()) {
            throw new RecordNotFoundException("Could not find any contracts in database.");
        } else {
            return serviceContracts;
        }
    }
}

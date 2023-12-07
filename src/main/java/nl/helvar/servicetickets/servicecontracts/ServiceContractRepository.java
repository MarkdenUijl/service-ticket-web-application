package nl.helvar.servicetickets.servicecontracts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceContractRepository extends JpaRepository<ServiceContract, Long> {
    List<ServiceContract> findByType(String type);
}

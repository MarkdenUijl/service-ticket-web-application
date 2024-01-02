package nl.helvar.servicetickets.servicecontracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ServiceContractRepository extends JpaRepository<ServiceContract, Long>, JpaSpecificationExecutor<ServiceContract> {
}

package nl.helvar.servicetickets.servicetickets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceTicketRepository extends JpaRepository<ServiceTicket, Long>, JpaSpecificationExecutor<ServiceTicket> {
}

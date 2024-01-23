package nl.helvar.servicetickets.ticketresponses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketResponseRepository extends JpaRepository<TicketResponse, Long>, JpaSpecificationExecutor<TicketResponse> {
}

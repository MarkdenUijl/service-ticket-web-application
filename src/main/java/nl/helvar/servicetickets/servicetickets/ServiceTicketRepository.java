package nl.helvar.servicetickets.servicetickets;

import nl.helvar.servicetickets.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceTicketRepository extends JpaRepository<ServiceTicket, Long> {
    @Query("SELECT t FROM ServiceTicket t WHERE " +
            "(:type IS NULL OR t.type = :type) AND " +
            "(:status IS NULL OR t.status = :status)")
    List<ServiceTicket> findAllByFilter (
            String type,
            String status
    );
}

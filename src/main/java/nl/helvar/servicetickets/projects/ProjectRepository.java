package nl.helvar.servicetickets.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE " +
            "(:name IS NULL OR p.name = :name) AND " +
            "(:address IS NULL OR p.address = :address)")
    List<Project> findAllByFilter (
            String name,
            String address
    );
}

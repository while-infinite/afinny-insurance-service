package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.OSAGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OSAGORepository extends JpaRepository<OSAGO, UUID> {
}

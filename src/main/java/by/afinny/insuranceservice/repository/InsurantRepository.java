package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Insurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsurantRepository extends JpaRepository<Insurant, UUID> {

    Optional<Insurant> findInsurantByClientId(String clientId);
}
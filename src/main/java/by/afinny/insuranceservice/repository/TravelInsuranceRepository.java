package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.TravelInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TravelInsuranceRepository extends JpaRepository<TravelInsurance, UUID> {
}

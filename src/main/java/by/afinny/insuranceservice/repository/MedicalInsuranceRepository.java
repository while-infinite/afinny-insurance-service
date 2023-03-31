package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.MedicalInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicalInsuranceRepository extends JpaRepository<MedicalInsurance, UUID> {
}
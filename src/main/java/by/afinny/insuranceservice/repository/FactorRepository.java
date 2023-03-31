package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Factor;
import by.afinny.insuranceservice.entity.constant.FactorName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactorRepository extends JpaRepository<Factor, FactorName> {
}

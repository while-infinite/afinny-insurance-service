package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.SumAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SumAssignmentRepository extends JpaRepository<SumAssignment, Integer> {

    List<SumAssignment> findSumAssignmentsByIsFlat(Boolean isFlat);

    Optional<SumAssignment> findSumAssignmentByIsFlatAndGeneralSum(Boolean isFlat, BigDecimal generalSum);
}
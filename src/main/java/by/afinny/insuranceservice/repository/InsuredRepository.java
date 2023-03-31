package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Insured;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InsuredRepository extends JpaRepository<Insured, UUID> {
}
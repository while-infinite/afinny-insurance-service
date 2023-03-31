package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.TravelProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TravelProgramRepository extends JpaRepository<TravelProgram, UUID> {
}

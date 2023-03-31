package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findAllApplicationByInsuranceStatus(InsuranceStatus status);


    @Query("SELECT a FROM Application a JOIN FETCH a.people p " +
            "WHERE a.id=:applicationId and p.insurant.clientId=:clientId")
    Optional<Application> findApplicationByClientIdAndApplicationId(@Param("clientId") String clientId,
                                                                    @Param("applicationId") UUID applicationId);
}
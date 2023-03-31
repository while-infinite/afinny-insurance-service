package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer>, JpaSpecificationExecutor<Program> {

    default Specification<Program> getProgramSpecification(Boolean isEmergencyHospitalization,
                                                          Boolean isDentalService,
                                                          Boolean isTelemedicine,
                                                          Boolean isEmergencyMedicalCare,
                                                          Boolean isCallingDoctor,
                                                          Boolean isOutpatientService) {
        return new Specification<Program>() {
            @Override
            public Predicate toPredicate(Root<Program> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();
                if (isEmergencyHospitalization)
                    predicates.add(builder.equal(root.get("isEmergencyHospitalization"), isEmergencyHospitalization));
                if (isDentalService)
                    predicates.add(builder.equal(root.get("isDentalService"), isDentalService));
                if (isTelemedicine)
                    predicates.add(builder.equal(root.get("isTelemedicine"), isTelemedicine));
                if (isEmergencyHospitalization)
                    predicates.add(builder.equal(root.get("isEmergencyMedicalCare"), isEmergencyMedicalCare));
                if (isEmergencyMedicalCare)
                    predicates.add(builder.equal(root.get("isCallingDoctor"), isCallingDoctor));
                if (isOutpatientService)
                    predicates.add(builder.equal(root.get("isOutpatientService"), isOutpatientService));
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
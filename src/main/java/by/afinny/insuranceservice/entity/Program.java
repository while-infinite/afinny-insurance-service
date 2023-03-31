package by.afinny.insuranceservice.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = Program.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class Program {

    public static final String TABLE_NAME = "program";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal sum;

    @Column(name = "organization", nullable = false, length = 30)
    private String organization;

    @Column(name = "link", nullable = false, length = 100)
    private String link;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_emergency_hospitalization", nullable = false)
    private Boolean isEmergencyHospitalization;

    @Column(name = "is_dental_service", nullable = false)
    private Boolean isDentalService;

    @Column(name = "is_telemedicine", nullable = false)
    private Boolean isTelemedicine;

    @Column(name = "is_emergency_medical_care", nullable = false)
    private Boolean isEmergencyMedicalCare;

    @Column(name = "is_calling_doctor", nullable = false)
    private Boolean isCallingDoctor;

    @Column(name = "is_outpatient_service", nullable = false)
    private Boolean isOutpatientService;
}
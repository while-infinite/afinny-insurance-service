package by.afinny.insuranceservice.entity;


import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.entity.constant.SportType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = Application.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class Application {

    public static final String TABLE_NAME = "application";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "insurance_sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal insuranceSum;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "period_of_insurance")
    @Enumerated(EnumType.STRING)
    private Period periodOfInsurance;

    @Column(name = "payment_cycle")
    @Enumerated(EnumType.STRING)
    private Period paymentCycle;

    @Column(name = "policy_sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal policySum;

    @Column(name = "insurance_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InsuranceStatus insuranceStatus;

    @Column(name = "region", length = 30)
    private String region;

    @Column(name = "district", length = 30)
    private String district;

    @Column(name = "city", length = 30)
    private String city;

    @Column(name = "street", length = 30)
    private String street;

    @Column(name = "house_number", length = 5)
    private String houseNumber;

    @Column(name = "flat_number", length = 4)
    private String flatNumber;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "insurance_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType;

    @Column(name = "failure_diagnosis_report")
    private String failureDiagnosisReport;

    @Column(name = "insurance_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private InsuranceCountry insuranceCountry;

    @Column(name = "sport_type")
    @Enumerated(EnumType.STRING)
    private SportType sportType;

    @Column(name = "last_date", nullable = false)
    private LocalDate lastDate;

    @OneToOne(mappedBy = "application",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private Agreement agreement;

    @OneToOne(mappedBy = "application",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private CarInsurance carInsurance;

    @OneToOne(mappedBy = "application")
    @ToString.Exclude
    private MedicalInsurance medicalInsurance;

    @OneToOne(mappedBy = "application",cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private PropertyInsurance propertyInsurance;

    @OneToOne(mappedBy = "application")
    private TravelInsurance travelInsurance;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "person_application",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    @ToString.Exclude
    private List<Person> people;

}
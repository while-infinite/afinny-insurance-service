package by.afinny.insuranceservice.entity;

import by.afinny.insuranceservice.entity.constant.DrivingExperience;
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
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = Insured.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class Insured {

    public static final String TABLE_NAME = "insured";

    @Id
    @Column(name = "person_id")
    private UUID id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @PrimaryKeyJoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "driving_experience")
    @Enumerated(EnumType.STRING)
    private DrivingExperience drivingExperience;

    @Column(name = "passport_number", length = 20)
    private String passportNumber;
}
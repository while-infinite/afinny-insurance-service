package by.afinny.insuranceservice.entity;

import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
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
import java.util.UUID;

@Entity
@Table(name = OSAGO.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class OSAGO {

    public static final String TABLE_NAME = "OSAGO";

    @Id
    @Column(name = "car_application_id")
    private UUID id;

    @OneToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "car_application_id", referencedColumnName = "application_id")
    private CarInsurance carInsurance;

    @Column(name = "category_group", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryGroup categoryGroup;

    @Column(name = "capacity_group", nullable = false)
    @Enumerated(EnumType.STRING)
    private CapacityGroup capacityGroup;

    @Column(name = "is_with_insured_accident", nullable = false)
    private Boolean isWithInsuredAccident;

    @Column(name = "model", nullable = false, length = 30)
    private String model;

    @Column(name = "car_number", nullable = false, length = 15)
    private String carNumber;
}
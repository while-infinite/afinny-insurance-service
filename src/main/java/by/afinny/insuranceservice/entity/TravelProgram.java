package by.afinny.insuranceservice.entity;

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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = TravelProgram.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class TravelProgram {

    public static final String TABLE_NAME = "travel_program";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "final_price", nullable = false, precision = 13, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "max_insurance_sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal maxInsuranceSum;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "travel_price_id")
    private TravelPrice travelPrice;

}

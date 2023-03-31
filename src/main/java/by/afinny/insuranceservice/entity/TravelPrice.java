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
import java.util.UUID;

@Entity
@Table(name = TravelPrice.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class TravelPrice {

    public static final String TABLE_NAME = "travel_price";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "basic_price", nullable = false, precision = 13, scale = 2)
    private BigDecimal basicPrice;

    @Column(name = "insured_number", nullable = false)
    private Integer insuredNumber;

    @Column(name = "is_with_sport_type")
    private Boolean isWithSportType;

    @Column(name = "is_with_PCR")
    private Boolean isWithRCR;

}

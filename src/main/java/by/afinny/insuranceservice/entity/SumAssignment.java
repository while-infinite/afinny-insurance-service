package by.afinny.insuranceservice.entity;

import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = SumAssignment.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class SumAssignment {

    public static final String TABLE_NAME = "sum_assignment";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "is_flat", nullable = false)
    private Boolean isFlat;

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemOfExpense name;

    @Column(name = "general_sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal generalSum;

    @Column(name = "sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal sum;

    @Column(name = "min_sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal minSum;

    @Column(name = "max_sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal maxSum;

    @Column(name = "default_sum", nullable = false, precision = 13, scale = 2)
    private BigDecimal defaultSum;
}
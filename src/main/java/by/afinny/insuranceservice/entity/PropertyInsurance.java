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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = PropertyInsurance.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class PropertyInsurance {

    public static final String TABLE_NAME = "property_insurance";

    @Id
    @Column(name = "application_id")
    private UUID id;

    @OneToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "application_id", referencedColumnName = "id")
    private Application application;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sum_assignment_id")
    private SumAssignment sumAssignment;
}
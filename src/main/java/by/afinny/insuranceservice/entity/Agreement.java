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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = Agreement.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class Agreement {

    public static final String TABLE_NAME = "agreement";

    @Id
    @Column(name = "application_id")
    private UUID id;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @PrimaryKeyJoinColumn(name = "application_id", referencedColumnName = "id")
    private Application application;

    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "agreement_date")
    private LocalDate agreementDate;

    @Column(name = "is_active")
    private Boolean isActive;
}
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
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = KASKO.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class KASKO {

    public static final String TABLE_NAME = "KASKO";

    @Id
    @Column(name = "car_application_id")
    private UUID id;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "car_application_id", referencedColumnName = "application_id")
    private CarInsurance carInsurance;
}
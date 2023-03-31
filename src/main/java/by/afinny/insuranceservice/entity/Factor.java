package by.afinny.insuranceservice.entity;

import by.afinny.insuranceservice.entity.constant.FactorName;
import by.afinny.insuranceservice.entity.constant.FactorRate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = Factor.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class Factor {

    public static final String TABLE_NAME = "factor";

    @Id
    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private FactorName name;

    @Column(name = "rate", nullable = false)
    @Enumerated(EnumType.STRING)
    private FactorRate rate;
}
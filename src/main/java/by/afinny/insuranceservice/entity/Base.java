package by.afinny.insuranceservice.entity;

import by.afinny.insuranceservice.entity.constant.BaseRate;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = Base.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class Base {

    public static final String TABLE_NAME = "base";

    @Id
    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryGroup name;

    @Column(name = "rate", nullable = false)
    @Enumerated(EnumType.STRING)
    private BaseRate rate;

    @ManyToOne
    @JoinColumn(name = "factor_name")
    private Factor factor;



}
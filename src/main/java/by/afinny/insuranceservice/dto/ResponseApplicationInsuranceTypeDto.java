package by.afinny.insuranceservice.dto;

import by.afinny.insuranceservice.entity.constant.InsuranceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class ResponseApplicationInsuranceTypeDto {

    private InsuranceType insuranceType;
}

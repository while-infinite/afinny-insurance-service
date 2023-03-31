package by.afinny.insuranceservice.dto;

import by.afinny.insuranceservice.entity.constant.InsuranceType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponsePaymentDetailsDto {

    private BigDecimal policySum;
    private InsuranceType insuranceType;
}

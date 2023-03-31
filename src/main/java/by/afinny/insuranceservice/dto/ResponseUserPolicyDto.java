package by.afinny.insuranceservice.dto;

import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseUserPolicyDto {

    private UUID applicationId;
    private InsuranceType insuranceType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String number;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate agreementDate;
    private LocalDate registrationDate;
    private InsuranceStatus insuranceStatus;
}
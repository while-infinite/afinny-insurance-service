package by.afinny.insuranceservice.dto;

import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
public class ResponseNewMedicinePolicyDto {

    private UUID applicationId;
    private LocalDate birthday;
    private String region;
    private String documentNumber;
    private DocumentType documentType;
    private BigDecimal insuranceSum;
    private BigDecimal policySum;
    private String firstName;
    private String middleName;
    private String lastName;
    private String passportNumber;
    private String email;
    private String phoneNumber;
    private Integer programId;
    private InsuranceType insuranceType;
    private LocalDate registrationDate;
    private Period periodOfInsurance;
    private Period paymentCycle;
    private String clientId;
}

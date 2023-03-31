package by.afinny.insuranceservice.dto;

import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.SportType;
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
public class RequestTravelPolicyDto {
    private InsuranceCountry insuranceCountry;
    private LocalDate birthday;
    private SportType sportType;
    private BigDecimal insuranceSum;
    private BigDecimal policySum;
    private LocalDate startDate;
    private LocalDate lastDate;
    private String firstName;
    private String middleName;
    private String lastName;
    private String passportNumber;
    private String email;
    private String phoneNumber;
    private InsuranceType insuranceType;
    private LocalDate registrationDate;
    private InsuranceStatus insuranceStatus;
    private String clientId;
    private UUID travelProgramId;
}

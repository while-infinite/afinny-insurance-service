package by.afinny.insuranceservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestNewRealEstatePolicy {

    private String city;
    private String clientId;
    private String district;
    private String email;
    private String firstName;
    private String flatNumber;
    private String houseNumber;
    private BigDecimal insuranceSum;
    private Boolean isFlat;
    private String lastName;
    private String middleName;
    private String paymentCycle;
    private String periodOfInsurance;
    private String phoneNumber;
    private String region;
    private LocalDate registrationDate;
    private String street;
    private String sumAssignmentName;
    private BigDecimal sumAssignmentSum;
    private BigDecimal policySum;
    private String insuranceType;
    private BigDecimal sumAssignmentGeneralSum;
    private BigDecimal sumAssignmentMinSum;
    private BigDecimal sumAssignmentMaxSum;
    private BigDecimal sumAssignmentDefaultSum;
    private String insuranceCountry;
    private LocalDate lastDate;
}

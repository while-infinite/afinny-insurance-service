package by.afinny.insuranceservice.dto;

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
public class ProgramDto {
    Boolean isEmergencyHospitalization;
    Boolean isDentalService;
    Boolean isTelemedicine;
    Boolean isEmergencyMedicalCare;
    Boolean isCallingDoctor;
    Boolean isOutpatientService;
    String name;
    String organization;
    String link;
    BigDecimal sum;
    String description;
    String programId;
}

package by.afinny.insuranceservice.dto.kafka;

import by.afinny.insuranceservice.dto.ResponseNewMedicinePolicyDto;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
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

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProducerNewMedicineEvent {

    private UUID id;
    private ResponseNewMedicinePolicyDto responseNewMedicinePolicyDto;
}

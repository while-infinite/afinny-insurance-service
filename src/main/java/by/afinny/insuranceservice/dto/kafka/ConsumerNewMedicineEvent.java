package by.afinny.insuranceservice.dto.kafka;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.UnicodeUtil;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ConsumerNewMedicineEvent {
    private UUID applicationId;
    private String insuranceStatus;
    private String number;
    private LocalDate startDate;
}

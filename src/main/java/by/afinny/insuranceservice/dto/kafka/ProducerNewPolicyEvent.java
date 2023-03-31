package by.afinny.insuranceservice.dto.kafka;

import by.afinny.insuranceservice.dto.RequestNewPolicy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProducerNewPolicyEvent {
    private String id;
    private RequestNewPolicy requestNewPolicy;
}

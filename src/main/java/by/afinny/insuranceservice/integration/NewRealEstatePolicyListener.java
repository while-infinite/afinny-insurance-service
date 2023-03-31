package by.afinny.insuranceservice.integration;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.service.InsurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewRealEstatePolicyListener {
    private final InsurantService insurantService;

    @KafkaListener(
            topics = "${kafka.topics.real-estate-listener.path}",
            groupId = "insurance-service",
            containerFactory = "kafkaListenerNewRealEstatePolicy"
    )
    public void onRequestInsertNewRealEstatePolicy(Message<ConsumerNewRealEstatePolicy> message) {
        ConsumerNewRealEstatePolicy event = message.getPayload();
        log.info("Processing event: {}", event);
        insurantService.saveNewRealEstatePolicy(event);
    }
}

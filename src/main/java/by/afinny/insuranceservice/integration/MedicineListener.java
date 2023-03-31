package by.afinny.insuranceservice.integration;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewMedicineEvent;
import by.afinny.insuranceservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MedicineListener {
    private final ApplicationService applicationService;

    @KafkaListener(
            topics = "${kafka.topics.insurance-service-listener.path}",
            groupId = "insurance-service",
            containerFactory = "kafkaListenerNewMedicinePolicy"
    )
    public void onRequestInsertNewPolicy(Message<ConsumerNewMedicineEvent> message) {
        log.info("onRequestInsertNewMedicinePolicy() invoke");
        ConsumerNewMedicineEvent event = message.getPayload();
        log.info("Processing event: {}", event);
        applicationService.saveNewMedicinePolicy(event);
    }
}

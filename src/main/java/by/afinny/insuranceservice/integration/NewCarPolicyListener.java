package by.afinny.insuranceservice.integration;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;
import by.afinny.insuranceservice.service.InsurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewCarPolicyListener {
    private final InsurantService insurantService;

    @KafkaListener(
            topics = "${kafka.topics.new-policy-listener.path}",
            groupId = "insurance-service",
            containerFactory = "kafkaListenerNewPolicy"
    )
    public void onRequestInsertNewPolicy(Message<ConsumerNewPolicyEvent> message) {

        ConsumerNewPolicyEvent event = message.getPayload();
        log.info("Processing event: {}", event);
        insurantService.saveNewPolicy(event);

    }
}

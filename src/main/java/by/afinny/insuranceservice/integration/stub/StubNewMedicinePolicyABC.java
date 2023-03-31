package by.afinny.insuranceservice.integration.stub;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewMedicineEvent;
import by.afinny.insuranceservice.dto.kafka.ProducerNewMedicineEvent;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class StubNewMedicinePolicyABC {

    private final KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.insurance-service-listener.path}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topics.insurance-service-producer.path}",
            groupId = "insurance-service",
            containerFactory = "stubKafkaListenerNewMedicinePolicy"

    )
    public void receiveProducerAndSendConsumerNewPolicyEvent(Message<ProducerNewMedicineEvent> message) {
        log.info("receiveProducerAndSendConsumerNewMedicinePolicyEvent() method invoked");
        ProducerNewMedicineEvent producerNewMedicineEvent = message.getPayload();
        ConsumerNewMedicineEvent consumerNewMedicineEvent = setUpEvent(producerNewMedicineEvent);
        sendEvent(consumerNewMedicineEvent);
    }

    private void sendEvent(ConsumerNewMedicineEvent event) {
        log.debug("sendEvent() method invoked");
        log.debug("send event: " + event);
        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(event)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build());
    }

    private ConsumerNewMedicineEvent setUpEvent(ProducerNewMedicineEvent event) {
        return ConsumerNewMedicineEvent.builder()
                .applicationId(event.getResponseNewMedicinePolicyDto().getApplicationId())
                .insuranceStatus(String.valueOf(InsuranceStatus.APPROVED))
                .number("7684774")
                .startDate(LocalDate.now().plusDays(3))
                .build();
    }
}

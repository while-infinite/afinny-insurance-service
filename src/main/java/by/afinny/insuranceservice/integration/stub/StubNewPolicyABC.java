package by.afinny.insuranceservice.integration.stub;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ProducerNewPolicyEvent;
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

import java.math.BigDecimal;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class StubNewPolicyABC {

    private final KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.new-policy-listener.path}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topics.new-policy-producer.path}",
            groupId = "insurance-service",
            containerFactory = "stubKafkaListenerNewPolicy"
    )
    public void receiveProducerAndSendConsumerNewPolicyEvent(Message<ProducerNewPolicyEvent> message) {
        log.info("receiveProducerAndSendConsumerNewPolicyEvent() method invoked");
        ProducerNewPolicyEvent producerNewPolicyEvent = message.getPayload();
        ConsumerNewPolicyEvent consumerNewPolicyEvent = setUpEvent(producerNewPolicyEvent);
        sendEvent(consumerNewPolicyEvent);
    }

    private void sendEvent(ConsumerNewPolicyEvent event) {
        log.debug("sendEvent() method invoked");
        log.debug("send event: " + event);
        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(event)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build());
    }

    private ConsumerNewPolicyEvent setUpEvent(ProducerNewPolicyEvent producerNewPolicyEvent) {
        log.debug("setUpEvent() method invoked");
        return ConsumerNewPolicyEvent.builder()
                .insuranceSum(createBigDecimal(10000, 70000))
                .periodOfInsurance(producerNewPolicyEvent.getRequestNewPolicy().getPeriodOfInsurance())
                .paymentCycle(producerNewPolicyEvent.getRequestNewPolicy().getPaymentCycle())
                .registrationDate(producerNewPolicyEvent.getRequestNewPolicy().getRegistrationDate())
                .policySum(producerNewPolicyEvent.getRequestNewPolicy().getPolicySum())
                .insuranceStatus(producerNewPolicyEvent.getRequestNewPolicy().getInsuranceStatus())
                .startDate(producerNewPolicyEvent.getRequestNewPolicy().getStartDate())
                .insuranceType(producerNewPolicyEvent.getRequestNewPolicy().getInsuranceType())
                .region(producerNewPolicyEvent.getRequestNewPolicy().getRegion())
                .firstName(producerNewPolicyEvent.getRequestNewPolicy().getFirstName())
                .middleName(producerNewPolicyEvent.getRequestNewPolicy().getMiddleName())
                .lastName(producerNewPolicyEvent.getRequestNewPolicy().getLastName())
                .phoneNumber(producerNewPolicyEvent.getRequestNewPolicy().getPhoneNumber())
                .clientId(producerNewPolicyEvent.getRequestNewPolicy().getClientId())
                .email(producerNewPolicyEvent.getRequestNewPolicy().getEmail())
                .isWithInsuredAccident(producerNewPolicyEvent.getRequestNewPolicy().getIsWithInsuredAccident())
                .capacityGroup(producerNewPolicyEvent.getRequestNewPolicy().getCapacityGroup())
                .categoryGroup(producerNewPolicyEvent.getRequestNewPolicy().getCategoryGroup())
                .birthday(producerNewPolicyEvent.getRequestNewPolicy().getBirthday())
                .passportNumber(producerNewPolicyEvent.getRequestNewPolicy().getPassportNumber())
                .drivingExperience(producerNewPolicyEvent.getRequestNewPolicy().getDrivingExperience())
                .model(producerNewPolicyEvent.getRequestNewPolicy().getModel())
                .carNumber(producerNewPolicyEvent.getRequestNewPolicy().getCarNumber())
                .factorName(producerNewPolicyEvent.getRequestNewPolicy().getFactorName())
                .insuranceCountry(producerNewPolicyEvent.getRequestNewPolicy().getInsuranceCountry())
                .lastDate(producerNewPolicyEvent.getRequestNewPolicy().getLastDate())
                .build();
    }


    private BigDecimal createBigDecimal(int min, int max) {
        Random random = new Random();
        return BigDecimal.valueOf(random.nextInt(((max - min) + 1) + min));

    }
}


package by.afinny.insuranceservice.integration.stub;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.kafka.ProducerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ProducerNewRealEstatePolicy;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class StubNewRealEstatePolicyABC {

    private final KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.real-estate-listener.path}")
    private String topic;

    @KafkaListener(
            topics = "${kafka.topics.real-estate-producer.path}",
            groupId = "insurance-service",
            containerFactory = "stubKafkaListenerNewRealEstatePolicy"
    )
    public void receiveProducerAndSendConsumerNewRealEstatePolicyEvent(Message<ProducerNewRealEstatePolicy> message) {
        log.info("receiveProducerAndSendConsumerNewRealEstatePolicyEvent() method invoked");
        ProducerNewRealEstatePolicy producerNewRealEstatePolicy = message.getPayload();
        ConsumerNewRealEstatePolicy consumerNewRealEstatePolicy = setUpEvent(producerNewRealEstatePolicy);
        sendEvent(consumerNewRealEstatePolicy);
    }

    private void sendEvent(ConsumerNewRealEstatePolicy event) {
        log.debug("sendEvent() method invoked");
        log.debug("send event: " + event);
        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(event)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build());
    }

    private ConsumerNewRealEstatePolicy setUpEvent (ProducerNewRealEstatePolicy producerNewRealEstatePolicy){
        log.debug("setUpEvent() method invoked");
        return ConsumerNewRealEstatePolicy.builder()
                .city(producerNewRealEstatePolicy.getCity())
                .clientId(producerNewRealEstatePolicy.getClientId())
                .district(producerNewRealEstatePolicy.getDistrict())
                .email(producerNewRealEstatePolicy.getEmail())
                .firstName(producerNewRealEstatePolicy.getFirstName())
                .flatNumber(producerNewRealEstatePolicy.getFlatNumber())
                .houseNumber(producerNewRealEstatePolicy.getHouseNumber())
                .insuranceStatus(String.valueOf(InsuranceStatus.APPROVED))
                .insuranceSum(producerNewRealEstatePolicy.getInsuranceSum())
                .isFlat(producerNewRealEstatePolicy.getIsFlat())
                .lastName(producerNewRealEstatePolicy.getLastName())
                .middleName(producerNewRealEstatePolicy.getMiddleName())
                .paymentCycle(producerNewRealEstatePolicy.getPaymentCycle())
                .periodOfInsurance(producerNewRealEstatePolicy.getPeriodOfInsurance())
                .phoneNumber(producerNewRealEstatePolicy.getPhoneNumber())
                .region(producerNewRealEstatePolicy.getRegion())
                .registrationDate(producerNewRealEstatePolicy.getRegistrationDate())
                .street(producerNewRealEstatePolicy.getStreet())
                .sumAssignmentName(producerNewRealEstatePolicy.getSumAssignmentName())
                .sumAssignmentSum(producerNewRealEstatePolicy.getSumAssignmentSum())
                .policySum(producerNewRealEstatePolicy.getPolicySum())
                .startDate(LocalDate.now().plusDays(1))
                .number(producerNewRealEstatePolicy.getClientId())
                .insuranceType(producerNewRealEstatePolicy.getInsuranceType())
                .sumAssignmentGeneralSum(producerNewRealEstatePolicy.getSumAssignmentGeneralSum())
                .sumAssignmentMaxSum(producerNewRealEstatePolicy.getSumAssignmentMaxSum())
                .sumAssignmentMinSum(producerNewRealEstatePolicy.getSumAssignmentMinSum())
                .sumAssignmentDefaultSum(producerNewRealEstatePolicy.getSumAssignmentDefaultSum())
                .build();
    }

}

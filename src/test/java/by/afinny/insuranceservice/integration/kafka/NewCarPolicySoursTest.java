package by.afinny.insuranceservice.integration.kafka;

import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.kafka.ProducerNewPolicyEvent;
import by.afinny.insuranceservice.integration.NewCarPolicySours;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class NewCarPolicySoursTest {

    @Autowired
    private NewCarPolicySours sours;

    @MockBean
    private KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.new-policy-producer.path}")
    private String KAFKA_TOPIC;
    private RequestNewPolicy requestNewPolicy;
    private ProducerNewPolicyEvent producerNewPolicyEvent;

    @BeforeAll
    void setUp(){
        requestNewPolicy = RequestNewPolicy.builder()
                .insuranceStatus("APPROVED")
                .registrationDate(LocalDate.of(2022,10,14))
                .startDate(LocalDate.of(2023, 10, 11))
                .periodOfInsurance("ONE_MONTH")
                .paymentCycle("THREE_MONTHS")
                .policySum(BigDecimal.valueOf(2))
                .region("1")
                .model("model")
                .carNumber("2222")
                .clientId("222222")
                .firstName("1")
                .middleName("1")
                .lastName("1")
                .isWithInsuredAccident(false)
                .categoryGroup("PASSENGER_AUTOMOBILE")
                .insuranceType("PROPERTY_INSURANCE")
                .phoneNumber("89992095590")
                .email("email")
                .capacityGroup("UP_TO_120_INCLUSIVE")
                .birthday(LocalDate.of(1993,03,13))
                .passportNumber("12354556")
                .drivingExperience("FROM_5_TO_15_YEARS")
                .factorName("MIDDLE")
                .build();

        producerNewPolicyEvent = ProducerNewPolicyEvent.builder()
                .id("12345")
                .requestNewPolicy(requestNewPolicy)
                .build();
    }

    @Test
    @DisplayName("verify sending message to kafka broker")
    void sendMessageAboutDeposit() {
        //ARRANGE
        ArgumentCaptor<Message<?>> messageCaptor = ArgumentCaptor.forClass(Message.class);

        //ACT
        sours.sendMessageAboutPolicyInformation(producerNewPolicyEvent);

        //VERIFY
        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<?> message = messageCaptor.getValue();

        assertThat(message.getPayload()).isEqualTo(producerNewPolicyEvent);
        assertThat(message.getHeaders()).containsEntry(KafkaHeaders.TOPIC, KAFKA_TOPIC);
    }
}



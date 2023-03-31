package by.afinny.insuranceservice.integration.kafka;

import by.afinny.insuranceservice.dto.kafka.ProducerNewRealEstatePolicy;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.integration.NewRealEstatePolicySours;
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

import static by.afinny.insuranceservice.entity.constant.InsuranceType.PROPERTY_INSURANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class NewRealEstatePolicySoursTest {

    @Autowired
    private NewRealEstatePolicySours sours;

    @MockBean
    private KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.real-estate-producer.path}")
    private String KAFKA_TOPIC;
    private ProducerNewRealEstatePolicy producerNewRealEstatePolicy;

    @BeforeAll
    void setUp() {
        producerNewRealEstatePolicy = ProducerNewRealEstatePolicy.builder()
                .city("1")
                .clientId("222222")
                .district("1")
                .email("email")
                .firstName("1")
                .flatNumber("1")
                .houseNumber("1")
                .insuranceSum(BigDecimal.valueOf(2))
                .isFlat(true)
                .lastName("1")
                .middleName("1")
                .paymentCycle(String.valueOf(Period.THREE_MONTHS))
                .periodOfInsurance(String.valueOf(Period.ONE_MONTH))
                .phoneNumber("89992095590")
                .region("1")
                .registrationDate(LocalDate.of(2022, 11, 12))
                .street("1")
                .sumAssignmentName(String.valueOf(ItemOfExpense.FLAT_FURNITURE))
                .sumAssignmentSum(BigDecimal.valueOf(12))
                .policySum(BigDecimal.valueOf(2))
                .insuranceType(String.valueOf(PROPERTY_INSURANCE))
                .sumAssignmentDefaultSum(BigDecimal.valueOf(30))
                .sumAssignmentMinSum(BigDecimal.valueOf(24))
                .sumAssignmentMaxSum(BigDecimal.valueOf(32))
                .sumAssignmentGeneralSum(BigDecimal.valueOf(30))
                .build();
    }

    @Test
    @DisplayName("verify sending message to kafka broker")
    void sendMessageAboutPolicy() {
        //ARRANGE
        ArgumentCaptor<Message<?>> messageCaptor = ArgumentCaptor.forClass(Message.class);

        //ACT
        sours.sendMessageAboutPolicyInformation(producerNewRealEstatePolicy);

        //VERIFY
        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<?> message = messageCaptor.getValue();

        assertThat(message.getPayload()).isEqualTo(producerNewRealEstatePolicy);
        assertThat(message.getHeaders()).containsEntry(KafkaHeaders.TOPIC, KAFKA_TOPIC);
    }

}
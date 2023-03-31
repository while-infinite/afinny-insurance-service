package by.afinny.insuranceservice.integration.kafka;

import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.SportType;
import by.afinny.insuranceservice.integration.NewTravelPolicySource;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class NewTravelPolicySourceTest {

    @Autowired
    private NewTravelPolicySource source;

    @MockBean
    private KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.insurance-travel-producer.path}")
    private String topic;
    private RequestTravelPolicyDto requestTravelPolicyDto;

    @BeforeAll
    void setUp() {
        requestTravelPolicyDto = RequestTravelPolicyDto.builder()
                .insuranceCountry(InsuranceCountry.AUSTRIA)
                .birthday(LocalDate.of(2022, 5, 10))
                .sportType(SportType.SPORT)
                .insuranceSum(BigDecimal.valueOf(2))
                .policySum(BigDecimal.valueOf(2))
                .startDate(LocalDate.of(2022, 5, 10))
                .lastDate(LocalDate.of(2022, 6, 10))
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .passportNumber("passportNumber")
                .email("email")
                .phoneNumber("phoneNumber")
                .insuranceType(InsuranceType.PROPERTY_INSURANCE)
                .registrationDate(LocalDate.of(2022, 5, 10))
                .insuranceStatus(InsuranceStatus.PENDING)
                .clientId("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb")
                .travelProgramId(UUID.randomUUID())
                .build();
    }

    @Test
    @DisplayName("verify sending message to kafka broker")
    void sendMessageAboutNewTravelPolicy() {
        //ARRANGE
        ArgumentCaptor<Message<?>> messageCaptor = ArgumentCaptor.forClass(Message.class);

        //ACT
        source.sendMessageAboutNewTravelPolicy(requestTravelPolicyDto);

        //VERIFY
        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<?> message = messageCaptor.getValue();

        assertThat(message.getPayload()).isEqualTo(requestTravelPolicyDto);
        assertThat(message.getHeaders()).containsEntry(KafkaHeaders.TOPIC, topic);
    }
}

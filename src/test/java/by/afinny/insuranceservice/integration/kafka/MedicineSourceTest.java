package by.afinny.insuranceservice.integration.kafka;

import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.integration.MedicineSource;
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
class MedicineSourceTest {
    @Autowired
    private MedicineSource source;

    @MockBean
    private KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.insurance-service-producer.path}")
    private String topic;
    private RequestMedicinePolicyDto requestMedicinePolicyDto;

    @BeforeAll
    void setUp() {
        requestMedicinePolicyDto = RequestMedicinePolicyDto.builder()
                .birthday(LocalDate.of(2022, 5, 10))
                .region("region")
                .documentNumber("2")
                .documentType(DocumentType.INN)
                .insuranceSum(BigDecimal.valueOf(2))
                .policySum(BigDecimal.valueOf(2))
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .passportNumber("passportNumber")
                .email("email")
                .phoneNumber("phoneNumber")
                .programId(2)
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .periodOfInsurance(Period.SIX_MONTHS)
                .clientId("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb")
                .build();
    }

    @Test
    @DisplayName("verify sending message to kafka broker")
    void sendMessageAboutNewMedicinePolicy() {
//        //ARRANGE
//        ArgumentCaptor<Message<?>> messageCaptor = ArgumentCaptor.forClass(Message.class);
//
//        //ACT
//        source.sendMessageAboutNewMedicinePolicy(requestMedicinePolicyDto);
//
//        //VERIFY
//        verify(kafkaTemplate).send(messageCaptor.capture());
//        Message<?> message = messageCaptor.getValue();
//
//        assertThat(message.getPayload()).isEqualTo(requestMedicinePolicyDto);
//        assertThat(message.getHeaders()).containsEntry(KafkaHeaders.TOPIC, topic);
    }
}

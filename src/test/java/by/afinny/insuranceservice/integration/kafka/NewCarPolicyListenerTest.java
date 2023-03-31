package by.afinny.insuranceservice.integration.kafka;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;
import by.afinny.insuranceservice.integration.NewCarPolicyListener;
import by.afinny.insuranceservice.service.InsurantService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class NewCarPolicyListenerTest {

    @InjectMocks
    private NewCarPolicyListener newCarPolicyListener;

    @Mock
    private InsurantService insurantService;

    private ConsumerNewPolicyEvent event;

    @BeforeAll
    void setUp() {
        event = ConsumerNewPolicyEvent.builder()
                .insuranceStatus("APPROVED")
                .insuranceSum(BigDecimal.valueOf(2))
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
    }

    @Test
    @DisplayName("Verify received message")
    void receiveDeposit_shouldInvokeSaveAgreement() {
        //ACT
        newCarPolicyListener.onRequestInsertNewPolicy(new GenericMessage<>(event));

        //VERIFY
        verify(insurantService, times(1)).saveNewPolicy(any(ConsumerNewPolicyEvent.class));
    }

}
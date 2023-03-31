package by.afinny.insuranceservice.integration.kafka;

import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.integration.NewRealEstatePolicyListener;
import by.afinny.insuranceservice.service.InsurantService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static by.afinny.insuranceservice.entity.constant.InsuranceType.PROPERTY_INSURANCE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class NewRealEstatePolicyListenerTest {

    @InjectMocks
    private NewRealEstatePolicyListener sours;

    @Mock
    private InsurantService insurantService;

    private ConsumerNewRealEstatePolicy event;

    @BeforeAll
    void setUp() {

        event = ConsumerNewRealEstatePolicy.builder()
                .city("1")
                .clientId("222222")
                .district("1")
                .email("email")
                .firstName("1")
                .flatNumber("1")
                .houseNumber("1")
                .insuranceStatus("APPROVED")
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
                .number("1")
                .policySum(BigDecimal.valueOf(2))
                .startDate(LocalDate.of(2023, 10, 11))
                .insuranceType(String.valueOf(PROPERTY_INSURANCE))
                .sumAssignmentDefaultSum(BigDecimal.valueOf(30))
                .sumAssignmentMinSum(BigDecimal.valueOf(24))
                .sumAssignmentMaxSum(BigDecimal.valueOf(32))
                .sumAssignmentGeneralSum(BigDecimal.valueOf(30))
                .build();

    }

    @Test
    @DisplayName("Verify received message")
    void receiveDeposit_shouldInvokeSaveAgreement() {
        //ACT
        sours.onRequestInsertNewRealEstatePolicy(new GenericMessage<>(event));

        //VERIFY
        verify(insurantService, times(1)).saveNewRealEstatePolicy(any(ConsumerNewRealEstatePolicy.class));

    }

}
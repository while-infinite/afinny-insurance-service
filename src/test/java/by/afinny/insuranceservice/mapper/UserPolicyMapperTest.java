package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.entity.Agreement;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class UserPolicyMapperTest {

    @InjectMocks
    private UserPolicyMapperImpl userPolicyMapper;

    private Application application;

    private Agreement agreement;

    @BeforeAll
    void setUp() {
        agreement = Agreement.builder()
                .number("1234567")
                .agreementDate(LocalDate.of(2022, 9, 25))
                .isActive(true)
                .build();

        application = Application.builder()
                .id(UUID.fromString("871f8834-5121-4fed-b5e4-cfd450c4f5c1"))
                .insuranceSum(BigDecimal.valueOf(300000.00))
                .registrationDate(LocalDate.of(2022, 10, 15))
                .periodOfInsurance(Period.THREE_MONTHS)
                .paymentCycle(Period.ONE_MONTH)
                .policySum(BigDecimal.valueOf(10000.00))
                .insuranceStatus(InsuranceStatus.PENDING)
                .region("region")
                .district("district")
                .city("city")
                .street("street")
                .houseNumber("11")
                .flatNumber("33")
                .startDate(LocalDate.now())
                .insuranceType(InsuranceType.MEDICAL_INSURANCE)
                .agreement(agreement)
                .build();

    }

    @Test
    @DisplayName("verify List<ResponseUserPolicyDto> fields settings")
    void applicationsToUserPolicyDto_shouldReturnListResponseUserPolicyDto() {
        List<ResponseUserPolicyDto> responseUserPolicyDtoList = userPolicyMapper.applicationsToUserPolicyDto(List.of(application));
        verifyResponseUserPolicyDto(responseUserPolicyDtoList.get(0));
    }

    @Test
    @DisplayName("verify ResponseUserPolicyDto fields settings")
    void applicationToUserPolicyDto_shouldReturnResponseUserPolicyDto() {
        ResponseUserPolicyDto responseUserPolicyDto = userPolicyMapper.applicationToUserPolicyDto(application);
        verifyResponseUserPolicyDto(responseUserPolicyDto);
    }

    private void verifyResponseUserPolicyDto(ResponseUserPolicyDto userPolicyDto) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(userPolicyDto.getApplicationId())
                    .isEqualTo(application.getId());
            softAssertions.assertThat(userPolicyDto.getInsuranceType())
                    .isEqualTo(application.getInsuranceType());
            softAssertions.assertThat(userPolicyDto.getNumber())
                    .isEqualTo(agreement.getNumber());
            softAssertions.assertThat(userPolicyDto.getAgreementDate())
                    .isEqualTo(agreement.getAgreementDate());
            softAssertions.assertThat(userPolicyDto.getRegistrationDate())
                    .isEqualTo(application.getRegistrationDate());
            softAssertions.assertThat(userPolicyDto.getInsuranceStatus())
                    .isEqualTo(application.getInsuranceStatus());
        });
    }
}
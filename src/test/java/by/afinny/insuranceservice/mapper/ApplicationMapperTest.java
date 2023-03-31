package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.Insured;
import by.afinny.insuranceservice.entity.MedicalInsurance;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.TravelInsurance;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.entity.constant.SportType;
import org.junit.jupiter.api.BeforeEach;
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
class ApplicationMapperTest {
    @InjectMocks
    private ApplicationMapperImpl applicationMapper;

    private Application application;

    private Application applicationAfterMapping;

    private Person personAfterMapping;

    private MedicalInsurance medicalInsuranceAfterMapping;

    private TravelInsurance travelInsuranceAfterMapping;

    private Insured insuredAfterMapping;

    private Insurant insurantAfterMapping;

    private ResponseApplicationInsuranceTypeDto responseApplicationInsuranceTypeDto;

    private ResponseRejectionLetterDto responseRejectionLetterDto;

    private RequestMedicinePolicyDto requestMedicinePolicyDto;

    private RequestTravelPolicyDto requestTravelPolicyDto;

    @BeforeEach
    void setUp() {
        application = Application.builder()
                .id(UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb"))
                .insuranceSum(new BigDecimal("2.00"))
                .registrationDate(LocalDate.of(2022, 12, 13))
                .periodOfInsurance(Period.ONE_MONTH)
                .paymentCycle(Period.ONE_MONTH)
                .policySum(new BigDecimal("2.00"))
                .insuranceStatus(InsuranceStatus.PENDING)
                .region("2")
                .district("2")
                .city("2")
                .street("2")
                .houseNumber("2")
                .flatNumber("2")
                .startDate(LocalDate.of(2022, 10, 13))
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .failureDiagnosisReport("2")
                .build();

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
    @DisplayName("check equals fields MedicalInsurance after mapping: passed if true")
    void toMedicalInsuranceCheckFields() {
        medicalInsuranceAfterMapping = applicationMapper.toMedicalInsurance(requestMedicinePolicyDto);
        assertSoftly(softAssertions -> softAssertions.assertThat(medicalInsuranceAfterMapping.getProgram().getId())
                .withFailMessage("Program should be equals")
                .isEqualTo(requestMedicinePolicyDto.getProgramId()));
    }

    @Test
    @DisplayName("check equals fields Person after mapping: passed if true")
    void toPersonCheckFields() {
        personAfterMapping = applicationMapper.toPerson(requestMedicinePolicyDto);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(personAfterMapping.getFirstName())
                    .withFailMessage("FirstName should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getFirstName());
            softAssertions.assertThat(personAfterMapping.getMiddleName())
                    .withFailMessage("MiddleName should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getMiddleName());
            softAssertions.assertThat(personAfterMapping.getLastName())
                    .withFailMessage("LastName should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getLastName());
        });
    }

    @Test
    @DisplayName("check equals fields Application after mapping: passed if true")
    void toApplicationCheckFields() {
        applicationAfterMapping = applicationMapper.toApplication(requestMedicinePolicyDto);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(applicationAfterMapping.getInsuranceSum())
                    .withFailMessage("InsuranceSum should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getInsuranceSum());
            softAssertions.assertThat(applicationAfterMapping.getPeriodOfInsurance())
                    .withFailMessage("PeriodOfInsurance should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getPeriodOfInsurance());
            softAssertions.assertThat(applicationAfterMapping.getPolicySum())
                    .withFailMessage("PolicySum should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getPolicySum());
            softAssertions.assertThat(applicationAfterMapping.getRegion())
                    .withFailMessage("Region should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getRegion());
            softAssertions.assertThat(applicationAfterMapping.getInsuranceType())
                    .withFailMessage("InsuranceType should be equals")
                    .isEqualTo(requestMedicinePolicyDto.getInsuranceType());
        });
    }

    @Test
    @DisplayName("check equals fields ResponseApplicationInsuranceTypeDto after mapping: passed if true")
    void toResponseApplicationDtoCheckFields() {
        responseApplicationInsuranceTypeDto = applicationMapper.toResponseTypes(List.of(application)).get(0);
        assertSoftly(softAssertions -> softAssertions.assertThat(application.getInsuranceType())
                .withFailMessage("Branch address should be equals")
                .isEqualTo(responseApplicationInsuranceTypeDto.getInsuranceType()));
    }

    @Test
    @DisplayName("check equals fields ResponseRejectionLetterDto after mapping: passed if true")
    void toResponseRejectionLetterDtoCheckFields() {
        responseRejectionLetterDto = applicationMapper.toResponseLetter(application);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(application.getId())
                    .withFailMessage("CardId should be equals")
                    .isEqualTo(responseRejectionLetterDto.getId());
            softAssertions.assertThat(application.getFailureDiagnosisReport())
                    .withFailMessage("Text report should be equals")
                    .isEqualTo(responseRejectionLetterDto.getFailureDiagnosisReport());
        });
    }

    @Test
    @DisplayName("verify ResponsePaymentDetailsDto fields settings")
    void toResponsePaymentDetails_shouldReturnResponsePaymentDetailsDto() {
        ResponsePaymentDetailsDto paymentDetailsDto = applicationMapper.toResponsePaymentDetails(application);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(paymentDetailsDto.getPolicySum())
                    .isEqualTo(application.getPolicySum());
            softAssertions.assertThat(paymentDetailsDto.getInsuranceType())
                    .isEqualTo(application.getInsuranceType());
        });
    }

    @Test
    @DisplayName("check equals fields TravelInsurance after mapping: passed if true")
    void toTravelInsuranceCheckFields() {
        travelInsuranceAfterMapping = applicationMapper.toTravelInsurance(requestTravelPolicyDto);
        assertSoftly(softAssertions -> softAssertions.assertThat(travelInsuranceAfterMapping.getTravelProgram().getId())
                .withFailMessage("Travel program should be equals")
                .isEqualTo(requestTravelPolicyDto.getTravelProgramId()));
    }

    @Test
    @DisplayName("check equals fields Person from requestTravelPolicyDto after mapping: passed if true")
    void toPersonFromTravelPolicyDtoCheckFields() {
        personAfterMapping = applicationMapper.toPerson(requestTravelPolicyDto);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(personAfterMapping.getFirstName())
                    .withFailMessage("FirstName should be equals")
                    .isEqualTo(requestTravelPolicyDto.getFirstName());
            softAssertions.assertThat(personAfterMapping.getMiddleName())
                    .withFailMessage("MiddleName should be equals")
                    .isEqualTo(requestTravelPolicyDto.getMiddleName());
            softAssertions.assertThat(personAfterMapping.getLastName())
                    .withFailMessage("LastName should be equals")
                    .isEqualTo(requestTravelPolicyDto.getLastName());
        });
    }

    @Test
    @DisplayName("check equals fields Application from requestTravelPolicyDto after mapping: passed if true")
    void toApplicationFromTravelPolicyDtoCheckFields() {
        applicationAfterMapping = applicationMapper.toApplication(requestTravelPolicyDto);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(applicationAfterMapping.getInsuranceSum())
                    .withFailMessage("InsuranceSum should be equals")
                    .isEqualTo(requestTravelPolicyDto.getInsuranceSum());
            softAssertions.assertThat(applicationAfterMapping.getRegistrationDate())
                    .withFailMessage("RegistrationDate should be equals")
                    .isEqualTo(requestTravelPolicyDto.getRegistrationDate());
            softAssertions.assertThat(applicationAfterMapping.getInsuranceCountry())
                    .withFailMessage("InsuranceCountry should be equals")
                    .isEqualTo(requestTravelPolicyDto.getInsuranceCountry());
            softAssertions.assertThat(applicationAfterMapping.getPolicySum())
                    .withFailMessage("PolicySum should be equals")
                    .isEqualTo(requestTravelPolicyDto.getPolicySum());
            softAssertions.assertThat(applicationAfterMapping.getInsuranceStatus())
                    .withFailMessage("InsuranceStatus should be equals")
                    .isEqualTo(requestTravelPolicyDto.getInsuranceStatus());
            softAssertions.assertThat(applicationAfterMapping.getSportType())
                    .withFailMessage("SportType should be equals")
                    .isEqualTo(requestTravelPolicyDto.getSportType());
            softAssertions.assertThat(applicationAfterMapping.getStartDate())
                    .withFailMessage("StartDate should be equals")
                    .isEqualTo(requestTravelPolicyDto.getStartDate());
            softAssertions.assertThat(applicationAfterMapping.getInsuranceType())
                    .withFailMessage("InsuranceType should be equals")
                    .isEqualTo(requestTravelPolicyDto.getInsuranceType());
            softAssertions.assertThat(applicationAfterMapping.getLastDate())
                    .withFailMessage("LastDate should be equals")
                    .isEqualTo(requestTravelPolicyDto.getLastDate());
        });
    }

    @Test
    @DisplayName("check equals fields Insured from requestTravelPolicyDto after mapping: passed if true")
    void toInsuredFromTravelPolicyDtoCheckFields() {
        insuredAfterMapping = applicationMapper.toInsured(requestTravelPolicyDto);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(insuredAfterMapping.getBirthday())
                    .withFailMessage("Birthday should be equals")
                    .isEqualTo(requestTravelPolicyDto.getBirthday());
            softAssertions.assertThat(insuredAfterMapping.getPassportNumber())
                    .withFailMessage("PassportNumber should be equals")
                    .isEqualTo(requestTravelPolicyDto.getPassportNumber());
        });
    }

    @Test
    @DisplayName("check equals fields Insurant from requestTravelPolicyDto after mapping: passed if true")
    void toInsurantFromTravelPolicyDtoCheckFields() {
        insurantAfterMapping = applicationMapper.toInsurant(requestTravelPolicyDto);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(insurantAfterMapping.getEmail())
                    .withFailMessage("Email should be equals")
                    .isEqualTo(requestTravelPolicyDto.getEmail());
            softAssertions.assertThat(insurantAfterMapping.getPhoneNumber())
                    .withFailMessage("PhoneNumber should be equals")
                    .isEqualTo(requestTravelPolicyDto.getPhoneNumber());
            softAssertions.assertThat(insurantAfterMapping.getClientId())
                    .withFailMessage("ClientId should be equals")
                    .isEqualTo(requestTravelPolicyDto.getClientId());
        });
    }
}
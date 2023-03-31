package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponseNewMedicinePolicyDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.dto.kafka.ProducerNewMedicineEvent;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.MedicalInsurance;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.Program;
import by.afinny.insuranceservice.entity.TravelInsurance;
import by.afinny.insuranceservice.entity.TravelProgram;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.entity.constant.SportType;
import by.afinny.insuranceservice.mapper.ApplicationMapperImpl;
import by.afinny.insuranceservice.repository.ApplicationRepository;
import by.afinny.insuranceservice.repository.MedicalInsuranceRepository;
import by.afinny.insuranceservice.repository.PersonRepository;
import by.afinny.insuranceservice.repository.TravelInsuranceRepository;
import by.afinny.insuranceservice.service.impl.ApplicationServiceImpl;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ApplicationServiceTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private MedicalInsuranceRepository medicalInsuranceRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Spy
    private ApplicationMapperImpl applicationMapper;
    @Mock
    private TravelInsuranceRepository travelInsuranceRepository;

    private final UUID APPLICATION_ID = UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb");

    private final UUID CLIENT_ID = UUID.randomUUID();

    private Application application;

    private MedicalInsurance medicalInsurance;

    private Person person;

    private TravelInsurance travelInsurance;

    private List<Application> applicationList;

    private RequestMedicinePolicyDto requestMedicinePolicyDto;

    private ResponsePaymentDetailsDto paymentDetailsDto;

    private RequestTravelPolicyDto requestTravelPolicyDto;

    private ProducerNewMedicineEvent producerNewMedicineEvent;

    private ResponseNewMedicinePolicyDto responseNewMedicinePolicyDto;

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

        person = Person.builder()
                .id(UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb"))
                .firstName("firtsName")
                .middleName("middleName")
                .lastName("lastName")
                .build();
        medicalInsurance = MedicalInsurance.builder()
                .id(UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb"))
                .program(new Program())
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


        responseNewMedicinePolicyDto = ResponseNewMedicinePolicyDto.builder()
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
                .registrationDate(LocalDate.now())
                .clientId("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb")
                .build();

        producerNewMedicineEvent = ProducerNewMedicineEvent.builder()
                .id(UUID.randomUUID())
                .responseNewMedicinePolicyDto(ResponseNewMedicinePolicyDto.builder()
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
                        .registrationDate(LocalDate.now())
                        .clientId("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb")
                        .build())
                .build();


        paymentDetailsDto = ResponsePaymentDetailsDto.builder()
                .policySum(new BigDecimal("2.00"))
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .build();

        applicationList = List.of(application);

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

        travelInsurance = TravelInsurance.builder()
                .id(UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb"))
                .travelProgram(new TravelProgram())
                .build();
    }

    @Test
    @DisplayName("Verify register new medicine policy.")
    void registerNewMedicinePolicyTest() {
        //ARRANGE
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(medicalInsuranceRepository.save(any(MedicalInsurance.class))).thenReturn(medicalInsurance);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        //ACT
        applicationService.registerNewMedicinePolicy(requestMedicinePolicyDto);
        //VERIFY
        Mockito.verify(applicationRepository, Mockito.times(1))
                .save(Mockito.any());
        Mockito.verify(personRepository, Mockito.times(1))
                .save(Mockito.any());
        Mockito.verify(medicalInsuranceRepository, Mockito.times(1))
                .save(Mockito.any());

    }

    @Test
    @DisplayName("If application hasn't been saved then throw exception")
    void registerNewMedicinePolicyIfApplicationNotSavedThenThrow() {
        // ARRANGE
        when(applicationRepository.save(any(Application.class))).thenThrow(RuntimeException.class);
        // ACT
        ThrowableAssert.ThrowingCallable registerNewMedicinePolicy =
                () -> applicationService.registerNewMedicinePolicy(requestMedicinePolicyDto);
        //VERIFY
        verify(eventPublisher, never()).publishEvent(requestMedicinePolicyDto);
        assertThatThrownBy(registerNewMedicinePolicy).isNotNull();
    }

    @Test
    @DisplayName("If medical insurance hasn't been saved then throw exception")
    void registerNewMedicinePolicyIfMedicalInsuranceNotSavedThenThrow() {
        // ARRANGE
        when(medicalInsuranceRepository.save(any(MedicalInsurance.class))).thenThrow(RuntimeException.class);
        // ACT
        ThrowableAssert.ThrowingCallable registerNewMedicinePolicy =
                () -> applicationService.registerNewMedicinePolicy(requestMedicinePolicyDto);
        //VERIFY
        verify(eventPublisher, never()).publishEvent(requestMedicinePolicyDto);
        assertThatThrownBy(registerNewMedicinePolicy).isNotNull();
    }

    @Test
    @DisplayName("If person hasn't been saved then throw exception")
    void registerNewMedicinePolicyIfPersonNotSavedThenThrow() {
        // ARRANGE
        when(personRepository.save(any(Person.class))).thenThrow(RuntimeException.class);
        // ACT
        ThrowableAssert.ThrowingCallable registerNewMedicinePolicy =
                () -> applicationService.registerNewMedicinePolicy(requestMedicinePolicyDto);
        //VERIFY
        verify(eventPublisher, never()).publishEvent(requestMedicinePolicyDto);
        assertThatThrownBy(registerNewMedicinePolicy).isNotNull();
    }

    @Test
    @DisplayName("check equals updateAt after mapping letter by id: passed if true")
    void getRejectionLetterShouldReturnResponseRejectionLetterDto() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(CLIENT_ID.toString(), APPLICATION_ID))
                .thenReturn(Optional.ofNullable(application));
        //ACT
        ResponseRejectionLetterDto rejectionLetter = applicationService.getRejectionLetter(CLIENT_ID, APPLICATION_ID);
        //VERIFY
        verifyResponseRejectionLetter(rejectionLetter);
    }

    @Test
    @DisplayName("when service throws exception return status BAD REQUEST")
    void getRejectionLetterIfNotSuccessThenThrow() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(CLIENT_ID.toString(), APPLICATION_ID))
                .thenThrow(RuntimeException.class);
        //ACT & VERIFY
        assertThatThrownBy(() -> applicationService.getRejectionLetter(CLIENT_ID, APPLICATION_ID))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("check equals updateAt after mapping: passed if true")
    void getInsuranceTypesShouldReturnListResponseApplicationDto() {
        //ARRANGE
        when(applicationRepository.findAll())
                .thenReturn(applicationList);
        //ACT
        List<ResponseApplicationInsuranceTypeDto> insuranceTypes = applicationService.getInsuranceTypes();
        //VERIFY
        verifyResponseApplicationDtoList(insuranceTypes);
    }

    @Test
    @DisplayName("when service throws exception return status BAD REQUEST")
    void getInsuranceTypesIfNotSuccessThenThrow() {
        //ARRANGE
        when(applicationRepository.findAll())
                .thenThrow(RuntimeException.class);
        //ACT & VERIFY
        assertThatThrownBy(() -> applicationService.getInsuranceTypes())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Return payment-details when application id was found.")
    void getPaymentDetails_shouldReturnResponsePaymentDetailsDto() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(CLIENT_ID.toString(), APPLICATION_ID))
                .thenReturn(Optional.of(application));
        when(applicationMapper.toResponsePaymentDetails(application))
                .thenReturn(paymentDetailsDto);
        //ACT
        ResponsePaymentDetailsDto responsePaymentDetailsDto = applicationService.getPaymentDetails(CLIENT_ID, APPLICATION_ID);
        //VERIFY
        assertThat(responsePaymentDetailsDto).isEqualTo(paymentDetailsDto);
    }

    @Test
    @DisplayName("If application with id wasn't found then throw EntityNotFoundException")
    void getPaymentDetails_ifApplicationNotFound_thenThrow() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(CLIENT_ID.toString(), APPLICATION_ID))
                .thenThrow(EntityNotFoundException.class);
        //ACT
        ThrowableAssert.ThrowingCallable createOrderMethodInvocation = () -> applicationService
                .getPaymentDetails(CLIENT_ID, APPLICATION_ID);
        //VERIFY
        assertThatThrownBy(createOrderMethodInvocation).isInstanceOf(EntityNotFoundException.class);
    }

    private void verifyResponseApplicationDtoList(List<ResponseApplicationInsuranceTypeDto> responseApplicationInsuranceTypeDtoList) {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(responseApplicationInsuranceTypeDtoList.isEmpty())
                .withFailMessage("List should not be empty")
                .isEqualTo(false);
        softly.assertThat(applicationList.size())
                .withFailMessage("List sizes should be equals")
                .isEqualTo(responseApplicationInsuranceTypeDtoList.size());
        softly.assertAll();
    }


    private void verifyResponseRejectionLetter(ResponseRejectionLetterDto rejectionLetter) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(application.getId())
                    .withFailMessage("CardId should be equals")
                    .isEqualTo(rejectionLetter.getId());
            softAssertions.assertThat(application.getFailureDiagnosisReport())
                    .withFailMessage("Text report should be equals")
                    .isEqualTo(rejectionLetter.getFailureDiagnosisReport());
        });
    }

    @Test
    @DisplayName("If application successfully deleted then return No Content")
    void deleteDebitCard_shouldReturnNoContent() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(CLIENT_ID.toString(), APPLICATION_ID))
                .thenReturn(Optional.of(application));
        //ACT
        applicationService.deleteApplication(CLIENT_ID, APPLICATION_ID);
        //VERIFY
        verify(applicationRepository).deleteById(APPLICATION_ID);
    }

    @Test
    @DisplayName("If application for deleting was not found then throw exception")
    void deleteCreditCard_ifCardNotFound_thenThrow() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(CLIENT_ID.toString(), APPLICATION_ID))
                .thenReturn(Optional.empty());
        //ACT
        ThrowableAssert.ThrowingCallable changeLimitMethod = () ->
                applicationService.deleteApplication(CLIENT_ID, APPLICATION_ID);
        //VERIFY
        assertThatThrownBy(changeLimitMethod).isInstanceOf(EntityNotFoundException.class);
        verify(applicationRepository, never()).deleteById(APPLICATION_ID);
    }

    @Test
    @DisplayName("Verify creating new travel policy.")
    void createNewTravelPolicyTest() {
        //ARRANGE
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(travelInsuranceRepository.save(any(TravelInsurance.class))).thenReturn(travelInsurance);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        //ACT
        applicationService.createNewTravelPolicy(requestTravelPolicyDto);
        //VERIFY
        Mockito.verify(applicationRepository, Mockito.times(1))
                .save(Mockito.any());
        Mockito.verify(personRepository, Mockito.times(1))
                .save(Mockito.any());
        Mockito.verify(travelInsuranceRepository, Mockito.times(1))
                .save(Mockito.any());
        verify(eventPublisher).publishEvent(requestTravelPolicyDto);
    }

    @Test
    @DisplayName("If application in createNewTravelPolicy hasn't been saved then throw exception")
    void createNewTravelPolicyIfApplicationNotSavedThenThrow() {
        // ARRANGE
        when(applicationRepository.save(any(Application.class))).thenThrow(RuntimeException.class);
        // ACT
        ThrowableAssert.ThrowingCallable createNewTravelPolicy =
                () -> applicationService.createNewTravelPolicy(requestTravelPolicyDto);
        //VERIFY
        verify(eventPublisher, never()).publishEvent(requestMedicinePolicyDto);
        assertThatThrownBy(createNewTravelPolicy).isNotNull();
    }

    @Test
    @DisplayName("If travel insurance hasn't been saved then throw exception")
    void createNewTravelPolicyIfMedicalInsuranceNotSavedThenThrow() {
        // ARRANGE
        when(travelInsuranceRepository.save(any(TravelInsurance.class))).thenThrow(RuntimeException.class);
        // ACT
        ThrowableAssert.ThrowingCallable createNewTravelPolicy =
                () -> applicationService.createNewTravelPolicy(requestTravelPolicyDto);
        //VERIFY
        verify(eventPublisher, never()).publishEvent(requestMedicinePolicyDto);
        assertThatThrownBy(createNewTravelPolicy).isNotNull();
    }

    @Test
    @DisplayName("If person in createNewTravelPolicy hasn't been saved then throw exception")
    void createNewTravelPolicyIfPersonNotSavedThenThrow() {
        // ARRANGE
        when(personRepository.save(any(Person.class))).thenThrow(RuntimeException.class);
        // ACT
        ThrowableAssert.ThrowingCallable createNewTravelPolicy =
                () -> applicationService.createNewTravelPolicy(requestTravelPolicyDto);
        //VERIFY
        verify(eventPublisher, never()).publishEvent(requestMedicinePolicyDto);
        assertThatThrownBy(createNewTravelPolicy).isNotNull();
    }
}
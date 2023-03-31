package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.entity.Agreement;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.Base;
import by.afinny.insuranceservice.entity.CarInsurance;
import by.afinny.insuranceservice.entity.Factor;
import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.Insured;
import by.afinny.insuranceservice.entity.OSAGO;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.PropertyInsurance;
import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.entity.constant.BaseRate;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.DrivingExperience;
import by.afinny.insuranceservice.entity.constant.FactorName;
import by.afinny.insuranceservice.entity.constant.FactorRate;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.mapper.PolicyMapper;
import by.afinny.insuranceservice.mapper.SumAssignmentMapper;
import by.afinny.insuranceservice.repository.AgreementRepository;
import by.afinny.insuranceservice.repository.ApplicationRepository;
import by.afinny.insuranceservice.repository.BaseRepository;
import by.afinny.insuranceservice.repository.CarInsuranceRepository;
import by.afinny.insuranceservice.repository.FactorRepository;
import by.afinny.insuranceservice.repository.InsurantRepository;
import by.afinny.insuranceservice.repository.InsuredRepository;
import by.afinny.insuranceservice.repository.OSAGORepository;
import by.afinny.insuranceservice.repository.PersonRepository;
import by.afinny.insuranceservice.repository.PropertyInsuranceRepository;
import by.afinny.insuranceservice.repository.SumAssignmentRepository;
import by.afinny.insuranceservice.service.impl.InsurantServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static by.afinny.insuranceservice.entity.constant.InsuranceType.PROPERTY_INSURANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PolicyServiceTest {

    @InjectMocks
    private InsurantServiceImpl insurantService;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private PolicyMapper policyMapper;
    @Mock
    private SumAssignmentMapper sumAssignmentMapper;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private InsurantRepository insurantRepository;
    @Mock
    private InsuredRepository insuredRepository;
    @Mock
    private FactorRepository factorRepository;
    @Mock
    private BaseRepository baseRepository;
    @Mock
    private OSAGORepository osagoRepository;
    @Mock
    private CarInsuranceRepository carInsuranceRepository;
    @Mock
    private PropertyInsuranceRepository propertyInsuranceRepository;
    @Mock
    private SumAssignmentRepository sumAssignmentRepository;
    @Mock
    private AgreementRepository agreementRepository;

    private Application application;
    private PolicyInfoDto policyInfoDto;
    private SumAssignment sumAssignment;
    private Person person;
    private Insurant insurant;
    private Agreement agreement;
    private OSAGO osago;
    private CarInsurance carInsurance;
    private Insured insured;
    private ConsumerNewPolicyEvent consumerNewPolicyEvent;
    private RequestNewPolicy requestNewPolicy;
    private Factor factor;
    private Base base;
    private ConsumerNewRealEstatePolicy consumerNewRealEstatePolicy;
    private PropertyInsurance propertyInsurance;

    private final UUID ALL_ID = UUID.fromString("a861c25d-7968-4f0b-b922-971d90c756a6");

    @BeforeEach
    void setUp() {
        agreement = Agreement.builder()
                .id(ALL_ID)
                .number("1")
                .agreementDate(LocalDate.of(2022, 11, 12))
                .isActive(true)
                .build();

        sumAssignment = SumAssignment.builder()
                .id(12)
                .isFlat(true)
                .name(ItemOfExpense.FLAT_FURNITURE)
                .sum(BigDecimal.valueOf(12))
                .minSum(BigDecimal.valueOf(24))
                .maxSum(BigDecimal.valueOf(32))
                .defaultSum(BigDecimal.valueOf(30))
                .generalSum(BigDecimal.valueOf(30))
                .build();


        insurant = Insurant.builder()
                .id(ALL_ID)
                .documentNumber("1")
                .documentType(DocumentType.INN)
                .email("email")
                .phoneNumber("89992095590")
                .clientId("222222")
                .build();

        insured = Insured.builder()
                .id(ALL_ID)
                .birthday(LocalDate.of(1993, 03, 13))
                .drivingExperience(DrivingExperience.FROM_5_TO_15_YEARS)
                .passportNumber("12354556")
                .build();


        person = Person.builder()
                .insurant(insurant)
                .id(ALL_ID)
                .firstName("1")
                .middleName("1")
                .lastName("1")
                .build();

        List<Person> people = List.of(person);


        osago = OSAGO.builder()
                .id(ALL_ID)
                .capacityGroup(CapacityGroup.UP_TO_120_INCLUSIVE)
                .isWithInsuredAccident(false)
                .model("model")
                .carNumber("2222")
                .build();
        factor = Factor.builder()
                .name(FactorName.MIDDLE)
                .rate(FactorRate.K2)
                .build();
        base = Base.builder()
                .name(CategoryGroup.PASSENGER_AUTOMOBILE)
                .rate(BaseRate.RUB_5000)
                .factor(factor)
                .build();
        carInsurance = CarInsurance.builder()
                .id(ALL_ID)
                .osago(osago)
                .base(base)
                .build();


        application = Application.builder()
                .agreement(agreement)
                .people(people)
                .propertyInsurance(PropertyInsurance.builder()
                        .id(ALL_ID)
                        .sumAssignment(sumAssignment)
                        .build())
                .carInsurance(carInsurance)
                .id(ALL_ID)
                .insuranceSum(BigDecimal.valueOf(20))
                .registrationDate(LocalDate.of(2022, 11, 12))
                .periodOfInsurance(Period.ONE_MONTH)
                .paymentCycle(Period.THREE_MONTHS)
                .policySum(BigDecimal.valueOf(2))
                .insuranceStatus(InsuranceStatus.APPROVED)
                .region("1")
                .district("1")
                .city("1")
                .street("1")
                .houseNumber("1")
                .flatNumber("1")
                .startDate(LocalDate.of(2023, 10, 11))
                .insuranceType(PROPERTY_INSURANCE)
                .failureDiagnosisReport("fail")
                .build();

        propertyInsurance = PropertyInsurance.builder()
                .id(ALL_ID)
                .application(application)
                .sumAssignment(sumAssignment)
                .build();


        policyInfoDto = PolicyInfoDto.builder()
                .insuranceStatus("APPROVED")
                .number("1")
                .agreementDate(LocalDate.of(2022, 11, 12))
                .startDate(LocalDate.of(2023, 10, 11))
                .periodOfInsurance("ONE_MONTH")
                .paymentCycle("THREE_MONTHS")
                .insuranceSum(BigDecimal.valueOf(2))
                .policySum(BigDecimal.valueOf(2))
                .region("1")
                .district("1")
                .city("1")
                .street("1")
                .houseNumber("1")
                .flatNumber("1")
                .model("model")
                .carNumber("2222")
                .clientId("222222")
                .isFlat(true)
                .firstName("1")
                .middleName("1")
                .lastName("1")
                .build();

        requestNewPolicy = RequestNewPolicy.builder()
                .insuranceStatus("APPROVED")
                .registrationDate(LocalDate.of(2022, 10, 14))
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
                .birthday(LocalDate.of(1993, 03, 13))
                .passportNumber("12354556")
                .drivingExperience("FROM_5_TO_15_YEARS")
                .factorName("MIDDLE")
                .build();

        consumerNewPolicyEvent = ConsumerNewPolicyEvent.builder()
                .insuranceStatus("APPROVED")
                .insuranceSum(BigDecimal.valueOf(2))
                .registrationDate(LocalDate.of(2022, 10, 14))
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
                .birthday(LocalDate.of(1993, 03, 13))
                .passportNumber("12354556")
                .drivingExperience("FROM_5_TO_15_YEARS")
                .factorName("MIDDLE")
                .build();

        consumerNewRealEstatePolicy = ConsumerNewRealEstatePolicy.builder()
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
    @DisplayName("If policy with incoming Id was found return policy information")
    void getPolicyInformation_returnPolicyInfoDto() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(ALL_ID.toString(), ALL_ID))
                .thenReturn(Optional.of(application));
        when(policyMapper.toPolicyInfoDto(application, agreement, osago, insurant, sumAssignment)).thenReturn(policyInfoDto);
        //ACT
        PolicyInfoDto result = insurantService.getPolicyInformation(ALL_ID, ALL_ID);
        //VERIFY
        assertThat(result).isNotNull();
        assertThat(result.toString()).isEqualTo(policyInfoDto.toString());
    }

    @Test
    @DisplayName("if policy with incoming  number wasn't found then throws EntityNotFoundException")
    void getCardInformation_ifNotSuccess_thenThrow() {
        //ARRANGE
        when(applicationRepository.findApplicationByClientIdAndApplicationId(ALL_ID.toString(), ALL_ID))
                .thenReturn(Optional.empty());
        //ACT
        ThrowableAssert.ThrowingCallable getCardBalanceMethodInvocation = ()
                -> insurantService.getPolicyInformation(ALL_ID, ALL_ID);
        //VERIFY
        assertThatThrownBy(getCardBalanceMethodInvocation).isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    @DisplayName("If success than save")
    void saveNewPolicy_mustSaveAllEntities() {
        //ARRANGE
        when(policyMapper.toPersonFromEvent(consumerNewPolicyEvent)).thenReturn(person);
        when(policyMapper.toApplication(consumerNewPolicyEvent)).thenReturn(application);
        when(policyMapper.toInsurant(consumerNewPolicyEvent, person)).thenReturn(insurant);
        when(policyMapper.toInsured(consumerNewPolicyEvent, person)).thenReturn(insured);


        //ACT
        insurantService.saveNewPolicy(consumerNewPolicyEvent);

        //VERIFY

        verify(applicationRepository).save(application);
        verify(personRepository).save(person);
        verify(insurantRepository).save(insurant);


    }

    @Test
    @DisplayName("Save all entities from ConsumerNewRealEstatePolicy")
    void saveNewRealEstatePolicy_mustSaveAllEntities() {
        //ARRANGE
        when(policyMapper.toApplication(consumerNewRealEstatePolicy)).thenReturn(application);
        when(policyMapper.toPersonFromEvent(consumerNewRealEstatePolicy)).thenReturn(person);
        when(policyMapper.toInsurant(consumerNewRealEstatePolicy, person)).thenReturn(insurant);
        when(sumAssignmentMapper.toSumAssignmentFromEvent(consumerNewRealEstatePolicy)).thenReturn(sumAssignment);
        when(policyMapper.toPropertyInsurance(application, sumAssignment)).thenReturn(propertyInsurance);
        when(policyMapper.toAgreement(consumerNewRealEstatePolicy, application)).thenReturn(agreement);

        //ACT
        insurantService.saveNewRealEstatePolicy(consumerNewRealEstatePolicy);

        //VERIFY
        verify(personRepository).save(person);
        verify(applicationRepository).save(application);
        verify(insurantRepository).save(insurant);
        verify(sumAssignmentRepository).save(sumAssignment);
        verify(propertyInsuranceRepository).save(propertyInsurance);
        verify(agreementRepository).save(agreement);


    }
}



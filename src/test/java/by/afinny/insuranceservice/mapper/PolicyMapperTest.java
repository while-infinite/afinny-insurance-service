package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
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

import static by.afinny.insuranceservice.entity.constant.InsuranceType.PROPERTY_INSURANCE;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@DisplayName("Verification of correct data generation. It will pass if the fields of the entity and dto are equal")
class PolicyMapperTest {

    @InjectMocks
    PolicyMapperImpl policyMapper;

    private Application application;
    private PolicyInfoDto policyInfoDto;
    private SumAssignment sumAssignment;
    private Person person;
    private Insurant insurant;
    private Agreement agreement;
    private OSAGO osago;
    private CarInsurance carInsurance;
    private ConsumerNewPolicyEvent consumerNewPolicyEvent;
    private Insured insured;
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
                .name(FactorName.HARD)
                .rate(FactorRate.K1)
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
    @DisplayName("Verify policy dto fields setting")
    void toPolicyInfoDto_shouldReturnCorrectMapping() {

        // ACT
        policyInfoDto = policyMapper.toPolicyInfoDto(application, agreement, osago, insurant, sumAssignment);

        //VERIFY
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(policyInfoDto.getInsuranceStatus())
                    .withFailMessage("Insurance Status should be equals")
                    .isEqualTo(application.getInsuranceStatus().toString());
            softAssertions.assertThat(policyInfoDto.getNumber())
                    .withFailMessage("Number should be equals")
                    .isEqualTo(agreement.getNumber());
            softAssertions.assertThat(policyInfoDto.getAgreementDate())
                    .withFailMessage("Agreement Date should be equals")
                    .isEqualTo(agreement.getAgreementDate());
            softAssertions.assertThat(policyInfoDto.getStartDate())
                    .withFailMessage("Start Date should be equals")
                    .isEqualTo(application.getStartDate());
            softAssertions.assertThat(policyInfoDto.getPeriodOfInsurance())
                    .withFailMessage("Period Of Insurance should be equals")
                    .isEqualTo(application.getPeriodOfInsurance().toString());
            softAssertions.assertThat(policyInfoDto.getPaymentCycle())
                    .withFailMessage("Payment Cycle should be equals")
                    .isEqualTo(application.getPaymentCycle().toString());
            softAssertions.assertThat(policyInfoDto.getInsuranceSum())
                    .withFailMessage("Insurance Sum should be equals")
                    .isEqualTo(application.getInsuranceSum());
            softAssertions.assertThat(policyInfoDto.getPolicySum())
                    .withFailMessage("Policy Sum should be equals")
                    .isEqualTo(application.getPolicySum());
            softAssertions.assertThat(policyInfoDto.getRegion())
                    .withFailMessage("Region should be equals")
                    .isEqualTo(application.getRegion());
            softAssertions.assertThat(policyInfoDto.getDistrict())
                    .withFailMessage("District should be equals")
                    .isEqualTo(application.getDistrict());
            softAssertions.assertThat(policyInfoDto.getStreet())
                    .withFailMessage("Street should be equals")
                    .isEqualTo(application.getStreet());
            softAssertions.assertThat(policyInfoDto.getClientId())
                    .withFailMessage("Client id should be equals")
                    .isEqualTo(insurant.getClientId());
            softAssertions.assertThat(policyInfoDto.getIsFlat())
                    .withFailMessage("Is flat should be equals")
                    .isEqualTo(sumAssignment.getIsFlat());

        });
    }

    @Test
    @DisplayName("Verify entities fields")
    void toEntitiesMapping() {
        application = policyMapper.toApplication(consumerNewPolicyEvent);
        person = policyMapper.toPersonFromEvent(consumerNewPolicyEvent);
        insurant = policyMapper.toInsurant(consumerNewPolicyEvent, person);
        insured = policyMapper.toInsured(consumerNewPolicyEvent, person);
        carInsurance = policyMapper.toCarInsurance(application, base);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(consumerNewPolicyEvent.getInsuranceStatus())
                    .withFailMessage("Insurance Status should be equals")
                    .isEqualTo(application.getInsuranceStatus().toString());
            softAssertions.assertThat(consumerNewPolicyEvent.getStartDate())
                    .withFailMessage("Start Date should be equals")
                    .isEqualTo(application.getStartDate());
            softAssertions.assertThat(consumerNewPolicyEvent.getPeriodOfInsurance())
                    .withFailMessage("Period Of Insurance should be equals")
                    .isEqualTo(application.getPeriodOfInsurance().toString());
            softAssertions.assertThat(consumerNewPolicyEvent.getPaymentCycle())
                    .withFailMessage("Payment Cycle should be equals")
                    .isEqualTo(application.getPaymentCycle().toString());
            softAssertions.assertThat(consumerNewPolicyEvent.getInsuranceSum())
                    .withFailMessage("Insurance Sum should be equals")
                    .isEqualTo(application.getInsuranceSum());
            softAssertions.assertThat(consumerNewPolicyEvent.getPolicySum())
                    .withFailMessage("Policy Sum should be equals")
                    .isEqualTo(application.getPolicySum());
            softAssertions.assertThat(consumerNewPolicyEvent.getRegion())
                    .withFailMessage("Region should be equals")
                    .isEqualTo(application.getRegion());

        });
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(consumerNewPolicyEvent.getFirstName())
                    .withFailMessage("First Name")
                    .isEqualTo(person.getFirstName());
            softAssertions.assertThat(consumerNewPolicyEvent.getMiddleName())
                    .withFailMessage("Middle Name")
                    .isEqualTo(person.getMiddleName());
            softAssertions.assertThat(consumerNewPolicyEvent.getLastName())
                    .withFailMessage("Last Name")
                    .isEqualTo(person.getLastName());
        });
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(consumerNewPolicyEvent.getClientId())
                    .withFailMessage("Client Id")
                    .isEqualTo(insurant.getClientId());
            softAssertions.assertThat(consumerNewPolicyEvent.getPhoneNumber())
                    .withFailMessage("Phone number")
                    .isEqualTo(insurant.getPhoneNumber());
            softAssertions.assertThat(consumerNewPolicyEvent.getEmail())
                    .withFailMessage("Email")
                    .isEqualTo(insurant.getEmail());
        });

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(consumerNewPolicyEvent.getBirthday())
                    .withFailMessage("Birthday")
                    .isEqualTo(insured.getBirthday());
            softAssertions.assertThat(consumerNewPolicyEvent.getPassportNumber())
                    .withFailMessage("Passport Number")
                    .isEqualTo(insured.getPassportNumber());
            softAssertions.assertThat(consumerNewPolicyEvent.getDrivingExperience())
                    .withFailMessage("Driving Experience")
                    .isEqualTo(insured.getDrivingExperience().toString());
        });


    }

    @Test
    void toEntityFromConsumerNewRealEstatePolicy() {
        application = policyMapper.toApplication(consumerNewRealEstatePolicy);
        person = policyMapper.toPersonFromEvent(consumerNewRealEstatePolicy);
        propertyInsurance = policyMapper.toPropertyInsurance(application, sumAssignment);
        insurant = policyMapper.toInsurant(consumerNewRealEstatePolicy, person);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(consumerNewRealEstatePolicy.getInsuranceStatus())
                    .withFailMessage("Insurance Status should be equals")
                    .isEqualTo(application.getInsuranceStatus().toString());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getStartDate())
                    .withFailMessage("Start Date should be equals")
                    .isEqualTo(application.getStartDate());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getPeriodOfInsurance())
                    .withFailMessage("Period Of Insurance should be equals")
                    .isEqualTo(application.getPeriodOfInsurance().toString());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getPaymentCycle())
                    .withFailMessage("Payment Cycle should be equals")
                    .isEqualTo(application.getPaymentCycle().toString());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getInsuranceSum())
                    .withFailMessage("Insurance Sum should be equals")
                    .isEqualTo(application.getInsuranceSum());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getPolicySum())
                    .withFailMessage("Policy Sum should be equals")
                    .isEqualTo(application.getPolicySum());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getRegion())
                    .withFailMessage("Region should be equals")
                    .isEqualTo(application.getRegion());

        });

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(consumerNewRealEstatePolicy.getFirstName())
                    .withFailMessage("First Name")
                    .isEqualTo(person.getFirstName());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getMiddleName())
                    .withFailMessage("Middle Name")
                    .isEqualTo(person.getMiddleName());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getLastName())
                    .withFailMessage("Last Name")
                    .isEqualTo(person.getLastName());
        });

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(consumerNewRealEstatePolicy.getClientId())
                    .withFailMessage("Client Id")
                    .isEqualTo(insurant.getClientId());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getPhoneNumber())
                    .withFailMessage("Phone number")
                    .isEqualTo(insurant.getPhoneNumber());
            softAssertions.assertThat(consumerNewRealEstatePolicy.getEmail())
                    .withFailMessage("Email")
                    .isEqualTo(insurant.getEmail());
        });


    }
}
package by.afinny.insuranceservice.integration.controller;

import by.afinny.insuranceservice.config.annotation.TestWithKafkaContainer;
import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.entity.Program;
import by.afinny.insuranceservice.entity.TravelInsurance;
import by.afinny.insuranceservice.entity.TravelPrice;
import by.afinny.insuranceservice.entity.TravelProgram;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.entity.constant.SportType;
import by.afinny.insuranceservice.mapper.PolicyMapper;
import by.afinny.insuranceservice.repository.ApplicationRepository;
import by.afinny.insuranceservice.repository.InsurantRepository;
import by.afinny.insuranceservice.repository.PersonRepository;
import by.afinny.insuranceservice.repository.ProgramRepository;
import by.afinny.insuranceservice.repository.TravelInsuranceRepository;
import by.afinny.insuranceservice.repository.TravelProgramRepository;
import by.afinny.insuranceservice.service.InsurantService;
import by.afinny.insuranceservice.utils.MappingUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static by.afinny.insuranceservice.entity.constant.InsuranceType.PROPERTY_INSURANCE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestWithKafkaContainer
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Integration test for InsuranceController")
public class InsuranceOrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MappingUtils mappingUtils;
    @Autowired
    PolicyMapper policyMapper;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InsurantService insurantService;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private InsurantRepository insurantRepository;

    @Autowired
    private TravelInsuranceRepository travelInsuranceRepository;

    @Autowired
    private TravelProgramRepository travelProgramRepository;


    private RequestNewPolicy requestNewPolicy;

    private RequestMedicinePolicyDto requestMedicinePolicyDto;

    private Program program;

    private UUID APPLICATION_ID = UUID.fromString("a861c25d-7968-4f0b-b922-971d90c756a6");

    private RequestNewRealEstatePolicy requestNewRealEstatePolicy;

    private RequestTravelPolicyDto requestTravelPolicyDto;

    private TravelProgram travelProgram;

    private TravelInsurance travelInsurance;


    @BeforeAll
    void setUp() {
        requestNewPolicy = RequestNewPolicy.builder()
                .insuranceStatus("APPROVED")
                .registrationDate(LocalDate.of(2022, 10, 14))
                .startDate(LocalDate.of(2023, 10, 11))
                .periodOfInsurance("ONE_MONTH")
                .paymentCycle("THREE_MONTHS")
                .policySum(BigDecimal.valueOf(2000))
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
                .insuranceCountry(String.valueOf(InsuranceCountry.AUSTRALIA))
                .lastDate(LocalDate.now().plusYears(5))
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
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .periodOfInsurance(Period.SIX_MONTHS)
                .clientId(APPLICATION_ID.toString())
                .insuranceCountry(String.valueOf(InsuranceCountry.AUSTRALIA))
                .lastDate(LocalDate.now().plusYears(10))
                .build();
        program = Program.builder()
                .name("name")
                .sum(new BigDecimal("100000.00"))
                .organization("organization")
                .link("link")
                .description("description")
                .isEmergencyHospitalization(true)
                .isDentalService(true)
                .isTelemedicine(true)
                .isEmergencyMedicalCare(true)
                .isCallingDoctor(true)
                .isOutpatientService(true)
                .build();
        requestNewRealEstatePolicy = RequestNewRealEstatePolicy.builder()
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
                .insuranceCountry(String.valueOf(InsuranceCountry.AUSTRALIA))
                .lastDate(LocalDate.now().plusYears(10))
                .build();
        requestTravelPolicyDto = RequestTravelPolicyDto.builder()
                .insuranceCountry(InsuranceCountry.AUSTRIA)
                .birthday(LocalDate.of(2022, 5, 10))
                .sportType(SportType.SPORT)
                .insuranceSum(BigDecimal.valueOf(2))
                .policySum(BigDecimal.valueOf(2))
                .startDate(LocalDate.of(2022, 6, 10))
                .lastDate(LocalDate.of(2028, 6, 10))
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .passportNumber("passportNumber")
                .email("email")
                .phoneNumber("phoneNumber")
                .insuranceType(InsuranceType.PROPERTY_INSURANCE)
                .registrationDate(LocalDate.of(2022, 5, 10))
                .insuranceStatus(InsuranceStatus.PENDING)
                .clientId(APPLICATION_ID.toString())
                .build();
        travelProgram = TravelProgram.builder()
                .id(APPLICATION_ID)
                .name("name")
                .description("description")
                .finalPrice(BigDecimal.valueOf(3000))
                .maxInsuranceSum(BigDecimal.valueOf(5000))
                .travelPrice(TravelPrice.builder()
                        .id(APPLICATION_ID)
                        .basicPrice(BigDecimal.valueOf(1000))
                        .insuredNumber(3)
                        .isWithSportType(true)
                        .isWithRCR(true)
                        .build())
                .build();

    }

    @BeforeEach
    void save() {
        program = programRepository.save(program);
        requestMedicinePolicyDto.setProgramId(program.getId());
        travelProgram = travelProgramRepository.save(travelProgram);
        requestTravelPolicyDto.setTravelProgramId(travelProgram.getId());

    }

    @Test
    @DisplayName("ifCreateNewPolicy_success_thenReturnStatusOk")
    void createNewPolicy_success_thenReturnStatusOk() throws Exception {
        //ACT
        ResultActions result = mockMvc.perform(post("/auth/insurance-order/new-cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mappingUtils.asJsonString(requestNewPolicy)));
        //VERIFY
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("ifRegisterNewMedicinePolicy_success_thenReturnStatusOk")
    void registerNewMedicinePolicy_success_thenReturnStatusOk() throws Exception {
        //ACT
        ResultActions result = mockMvc.perform(post("/auth/insurance-order/new-medicine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mappingUtils.asJsonString(requestMedicinePolicyDto)));
        //VERIFY
        result.andExpect(status().isOk());

        Assertions.assertThat(applicationRepository.findAll())
                .isNotEmpty();
        Assertions.assertThat(insurantRepository.findAll())
                .isNotEmpty();
        Assertions.assertThat(personRepository.findAll())
                .isNotEmpty();
    }

    @Test
    @DisplayName("ifCreateNewRealEstatePolicy_success_thenReturnStatusOk")
    void createNewRealEstatePolicy_success_thenReturnStatusOk() throws Exception {
        //ACT
        ResultActions result = mockMvc.perform(post("/auth/insurance-order/new-property")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mappingUtils.asJsonString(requestNewRealEstatePolicy)));
        //VERIFY
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("ifCreateNewTravelPolicy_success_thenReturnStatusOk")
    void createNewTravelPolicy_success_thenReturnStatusOk() throws Exception {
        //ACT
        ResultActions result = mockMvc.perform(post("/auth/insurance-order/new-travel-program")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mappingUtils.asJsonString(requestTravelPolicyDto)));
        //VERIFY
        result.andExpect(status().isOk());
    }


}

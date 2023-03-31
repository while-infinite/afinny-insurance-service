package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.dto.ResponseTravelPolicyDto;
import by.afinny.insuranceservice.entity.constant.DocumentType;
import by.afinny.insuranceservice.entity.constant.InsuranceCountry;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.entity.constant.SportType;
import by.afinny.insuranceservice.service.ApplicationService;
import by.afinny.insuranceservice.service.InsurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InsuranceOrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class InsuranceOrderControllerTest {

    @MockBean
    private InsurantService insurantService;
    @MockBean
    private ApplicationService applicationService;

    private UUID APPLICATION_ID = UUID.fromString("a861c25d-7968-4f0b-b922-971d90c756a6");
    private UUID RANDOM_ID = UUID.randomUUID();
    @Autowired
    private MockMvc mockMvc;
    private PolicyInfoDto policyInfoDto;
    private RequestNewPolicy requestNewPolicy;
    private RequestMedicinePolicyDto requestMedicinePolicyDto;
    private RequestTravelPolicyDto requestTravelPolicyDto;
    private RequestNewRealEstatePolicy requestNewRealEstatePolicy;

    @BeforeEach
    void setUp() {

        requestNewPolicy = RequestNewPolicy.builder()
                .insuranceStatus("APPROVED")
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
                .clientId(APPLICATION_ID.toString())
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
                .clientId(APPLICATION_ID.toString())
                .travelProgramId(RANDOM_ID)
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
                .insuranceType(String.valueOf(InsuranceType.PROPERTY_INSURANCE))
                .sumAssignmentDefaultSum(BigDecimal.valueOf(30))
                .sumAssignmentMinSum(BigDecimal.valueOf(24))
                .sumAssignmentMaxSum(BigDecimal.valueOf(32))
                .sumAssignmentGeneralSum(BigDecimal.valueOf(30))
                .build();
    }

    @Test
    @DisplayName("The new insurance policy was successfully saved")
    void newInsurancePolicy_was_saved() throws Exception {
        //ACT & VERIFY
        mockMvc.perform(
                        post("/auth/insurance-order/new-cars")
                                .contentType("application/json")
                                .content(asJsonString(requestNewPolicy)))
                .andExpect(status().isOk());

        verify(insurantService)
                .createNewPolicy( any(RequestNewPolicy.class));

    }

    @Test
    @DisplayName("If new insurance policy wasn't successfully saved then return status INTERNAL SERVER ERROR")
    void newInsurancePolicy_was_not_saved_thenStatus500() throws Exception {
        //ARRANGE
        doThrow(new RuntimeException())
                .when(insurantService)
                .createNewPolicy(any(RequestNewPolicy.class));

        //ACT & VERIFY
        mockMvc.perform(
                        post("/auth/insurance-order/new-cars")
                                .contentType("application/json")
                                .content(asJsonString(requestNewPolicy)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("If new medicine insurance successfully registered then return client")
    void registerNewMedicineInsuranceReturnOk() throws Exception {
        //ACT & VERIFY
        mockMvc.perform(
                        post("/auth/insurance-order/new-medicine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(requestMedicinePolicyDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("If new travel insurance successfully created then return responseTravelPolicyDto")
    void createNewTravelPolicyReturnResponseTravelPolicyDto() throws Exception {
        //ACT & VERIFY
        mockMvc.perform(
                        post("/auth/insurance-order/new-travel-program")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(requestTravelPolicyDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("The new Real Estate policy was successfully saved")
    void newRealEstatePolicy_was_saved () throws Exception {
        //ACT & VERIFY
        mockMvc.perform(
                        post("/auth/insurance-order/new-property")
                                .contentType("application/json")
                                .content(asJsonString(requestNewRealEstatePolicy)))
                .andExpect(status().isOk());

        verify(insurantService)
                .createNewRealEstatePolicy( any(RequestNewRealEstatePolicy.class));
    }

    @Test
    @DisplayName("The new Real Estate policy wasn't successfully saved then return status INTERNAL SERVER ERROR")
    void newRealEstatePolicy_was_not_saved_thenStatus500() throws Exception {
        //ARRANGE
        doThrow(new RuntimeException())
                .when(insurantService)
                .createNewRealEstatePolicy(any(RequestNewRealEstatePolicy.class));

        //ACT & VERIFY
        mockMvc.perform(
                        post("/auth/insurance-order/new-property")
                                .contentType("application/json")
                                .content(asJsonString(requestNewRealEstatePolicy)))
                .andExpect(status().isInternalServerError());
    }



    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }

    private void verifyBody(String actualBody, String expectedBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}
package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.constant.InsuranceStatus;
import by.afinny.insuranceservice.entity.constant.InsuranceType;
import by.afinny.insuranceservice.entity.constant.Period;
import by.afinny.insuranceservice.service.ApplicationService;
import by.afinny.insuranceservice.service.InsurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(InsuranceController.class)
@ActiveProfiles("test")
class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private InsurantService insurantService;

    private List<ResponseApplicationInsuranceTypeDto> responseApplicationInsuranceTypeDtoList;

    private List<ResponseUserPolicyDto> responseUserPolicyDtoList;

    private ResponseRejectionLetterDto responseRejectionLetterDto;

    private ResponsePaymentDetailsDto responsePaymentDetailsDto;

    private final UUID APPLICATION_ID = UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb");

    private final UUID CARD_ID = UUID.fromString("c222ff7e-e6ab-4cf1-ad11-fb2b07abc1bb");

    private PolicyInfoDto policyInfoDto;

    private final UUID CLIENT_ID = UUID.randomUUID();

    private Application application;

    @BeforeEach
    void setUp() {
        ResponseApplicationInsuranceTypeDto responseApplicationInsuranceTypeDto = ResponseApplicationInsuranceTypeDto.builder()
                .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                .build();

        responseApplicationInsuranceTypeDtoList = List.of(responseApplicationInsuranceTypeDto);

        responseUserPolicyDtoList = List.of(ResponseUserPolicyDto.builder()
                        .insuranceType(InsuranceType.MEDICAL_INSURANCE)
                        .registrationDate(LocalDate.of(2022, 10, 15))
                        .insuranceStatus(InsuranceStatus.PENDING)
                        .number("12345")
                        .agreementDate(LocalDate.of(2022, 3, 23))
                        .build(),
                ResponseUserPolicyDto.builder()
                        .insuranceType(InsuranceType.CAR_INSURANCE_OSAGO)
                        .registrationDate(LocalDate.of(2022, 5, 10))
                        .insuranceStatus(InsuranceStatus.APPROVED)
                        .number("67890")
                        .agreementDate(LocalDate.of(2022, 5, 5))
                        .build());

        responseRejectionLetterDto = ResponseRejectionLetterDto.builder()
                .id(APPLICATION_ID)
                .failureDiagnosisReport("Text")
                .build();

        responsePaymentDetailsDto = ResponsePaymentDetailsDto.builder()
                .policySum(new BigDecimal("2.00"))
                .insuranceType(InsuranceType.MEDICAL_INSURANCE)
                .build();

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

        policyInfoDto = PolicyInfoDto.builder()
                .insuranceStatus("APPROVED")
                .number("1")
                .agreementDate(LocalDate.of(2022, 11, 12))
                .startDate(LocalDate.of(2023, 10, 11))
                .periodOfInsurance("ONE_MONTH")
                .paymentCycle("THREE_MONTHS")
                .insuranceSum(BigDecimal.valueOf(20))
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

    }

    @Test
    @DisplayName("if the list of responseApplicationDto was successfully received then return status OK")
    void getInsuranceTypes_shouldReturnResponseApplicationDto() throws Exception {
        //ARRANGE
        when(applicationService.getInsuranceTypes())
                .thenReturn(responseApplicationInsuranceTypeDtoList);
        //ACT
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/insurance/types"))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyBody(asJsonString(responseApplicationInsuranceTypeDtoList), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the list of responseApplicationDto wasn't successfully received then return Internal Server Error")
    void getInsuranceTypes_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(applicationService.getInsuranceTypes())
                .thenThrow(new RuntimeException());
        //ACT & VERIFY
        mockMvc
                .perform(get("/auth/insurance/types"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("if the list of user policies was successfully received then return status OK")
    void getAllUserPolicies_shouldReturnAllUserPolicies() throws Exception {
        //ARRANGE
        when(insurantService.getAllUserPolicies(CLIENT_ID.toString())).thenReturn(responseUserPolicyDtoList);

        //ACT
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/insurance/")
                        .param("clientId", CLIENT_ID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(responseUserPolicyDtoList)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseUserPolicyDtoList), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the list of user policies wasn't successfully received then return Internal Server Error")
    void getAllUserPolicies_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(insurantService.getAllUserPolicies(any())).thenThrow(new RuntimeException());

        //ACT & VERIFY
        mockMvc.perform(get("/auth/insurance/")
                        .param("clientId", CLIENT_ID.toString()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("if the responseRejectionLetterDto was successfully received then return status OK")
    void getRejectionLetter_shouldReturnResponseRejectionLetterDto() throws Exception {
        //ARRANGE
        when(applicationService.getRejectionLetter(CLIENT_ID, APPLICATION_ID))
                .thenReturn(responseRejectionLetterDto);
        //ACT
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/insurance/{application-id}/report", APPLICATION_ID)
                        .param("clientId", CLIENT_ID.toString()))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyBody(asJsonString(responseRejectionLetterDto), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the responseRejectionLetterDto wasn't successfully received then return Internal Server Error")
    void getRejectionLetter_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(applicationService.getRejectionLetter(CLIENT_ID, APPLICATION_ID))
                .thenThrow(new RuntimeException());
        //ACT & VERIFY
        mockMvc
                .perform(get("/auth/insurance/{application-id}/report", APPLICATION_ID)
                        .param("clientId", CLIENT_ID.toString()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("if the payment details were successfully received then return status OK")
    void getPaymentDetails_shouldReturnResponsePaymentDetailsDto() throws Exception {
        //ARRANGE
        when(applicationService.getPaymentDetails(CLIENT_ID, APPLICATION_ID)).thenReturn(responsePaymentDetailsDto);

        //ACT
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/insurance/{application-id}/payment-details", APPLICATION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(responsePaymentDetailsDto))
                        .param("clientId", CLIENT_ID.toString()))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responsePaymentDetailsDto), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the payment details weren't successfully received then return Internal Server Error")
    void getPaymentDetails_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(applicationService.getPaymentDetails(any(UUID.class), any(UUID.class))).thenThrow(new RuntimeException());
        //ACT & VERIFY
        mockMvc.perform(get("/auth/insurance/{application-id}/payment-details", APPLICATION_ID)
                        .param("clientId", CLIENT_ID.toString()))
                .andExpect(status().isInternalServerError());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(obj);
    }

    @Test
    @DisplayName("Information about the insurance policy was successfully found")
    void getPolicyInformation_shouldReturnPolicyInfo() throws Exception {
        //ARRANGE
        when(insurantService.getPolicyInformation(CLIENT_ID, APPLICATION_ID)).thenReturn(policyInfoDto);

        //ACT
        MvcResult result = mockMvc.perform(
                        get("/auth/insurance/{applicationId}", APPLICATION_ID.toString())
                                .param("clientId", CLIENT_ID.toString()))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(policyInfoDto), result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if policy wasn't successfully found then return status INTERNAL_SERVER_ERROR")
    void getPolicyInformation_ifNotFoundPolicy_then500_INTERNAL_SERVER_ERROR() throws Exception {
        //ARRANGE
        when(insurantService.getPolicyInformation(any(UUID.class), any(UUID.class))).thenThrow(new RuntimeException());

        //ACT & VERIFY
        mockMvc.perform(
                        get("/auth/insurance/{applicationId}", APPLICATION_ID.toString())
                                .param("clientId", CLIENT_ID.toString()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("If application was successfully delete then return status No_Content")
    void deleteApplication_ifSuccessfullyDeleted_then204_NO_CONTENT() throws Exception {
        //ARRANGE
        ArgumentCaptor<UUID> operationIdCaptor = ArgumentCaptor.forClass(UUID.class);

        //ACT
        ResultActions perform = mockMvc.perform(
                delete(InsuranceController.URL_INSURANCE
                        + InsuranceController.URL_REVOCATION, application.getId())
                        .param("clientId", CLIENT_ID.toString()));

        //VERIFY
        perform.andExpect(status().isNoContent());
        verify(applicationService, times(1)).deleteApplication(any(UUID.class),
                operationIdCaptor.capture());
        assertThat(CARD_ID).isEqualTo(operationIdCaptor.getValue());
    }

    @Test
    @DisplayName("If application wasn't successfully delete then return status BAD_REQUEST")
    void deleteApplication_ifNotDeleted_then400_BAD_REQUEST() throws Exception {
        //ARRANGE
        doThrow(EntityNotFoundException.class).when(applicationService)
                .deleteApplication(CLIENT_ID, APPLICATION_ID);

        //ACT
        ResultActions perform = mockMvc.perform(
                delete(InsuranceController.URL_INSURANCE
                        + InsuranceController.URL_REVOCATION, application.getId())
                        .param("clientId", CLIENT_ID.toString()));

        //VERIFY
        perform.andExpect(status().isBadRequest());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

}
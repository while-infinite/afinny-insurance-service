package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.CoefficientsCalculationDto;
import by.afinny.insuranceservice.dto.FactorDto;
import by.afinny.insuranceservice.dto.ResponseFinalPriceDto;
import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DrivingExperience;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.service.FactorService;
import by.afinny.insuranceservice.service.SumAssignmentService;
import by.afinny.insuranceservice.service.TravelProgramService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(CalculationController.class)
@ActiveProfiles("test")
class CalculationControllerTest {

    @MockBean
    private SumAssignmentService sumAssignmentService;

    @MockBean
    private FactorService factorService;

    @MockBean
    private TravelProgramService travelProgramService;

    @Autowired
    private MockMvc mockMvc;

    private List<ResponseGeneralSumDto> responseGeneralSumDtoList;

    private ResponseSumAssignmentDto responseSumAssignmentDto;

    private CoefficientsCalculationDto coefficientsCalculationDto;

    private ResponseFinalPriceDto responseFinalPriceDto;

    @BeforeEach
    void setUp() {
        responseGeneralSumDtoList = List.of(ResponseGeneralSumDto.builder()
                        .generalSum("100000.00").build(),
                ResponseGeneralSumDto.builder()
                        .generalSum("70000.00").build());

        responseSumAssignmentDto = ResponseSumAssignmentDto.builder()
                .id(String.valueOf(1))
                .name(String.valueOf(ItemOfExpense.FLAT_FURNITURE))
                .minSum(String.valueOf(new BigDecimal("90000.00")))
                .maxSum(String.valueOf(new BigDecimal("110000.00")))
                .defaultSum(String.valueOf(new BigDecimal("101000.00")))
                .build();

        coefficientsCalculationDto = CoefficientsCalculationDto.builder()
                .baseRate(7000)
                .factors(List.of(FactorDto.builder()
                        .factorName("HARD")
                        .factorRate(1.3)
                        .build(), FactorDto.builder()
                        .factorName("MIDDLE")
                        .factorRate(1.2)
                        .build(), FactorDto.builder()
                        .factorName("LIGHT")
                        .factorRate(1.1)
                        .build(), FactorDto.builder()
                        .factorName("HARD")
                        .factorRate(1.3)
                        .build()))
                .build();

        responseFinalPriceDto = ResponseFinalPriceDto.builder()
                .finalPrice(61990)
                .build();
    }

    @Test
    @DisplayName("if the list of general sum was successfully received then return status OK")
    void getGeneralSum_shouldReturnGeneralSumDtoList() throws Exception {
        //ARRANGE
        when(sumAssignmentService.getGeneralSumByIsFlat(true)).thenReturn(responseGeneralSumDtoList);

        //ACT
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/calculations/property/sum")
                        .param("isFlat", String.valueOf(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(responseGeneralSumDtoList)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseGeneralSumDtoList), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the list of general sum wasn't successfully received then return Internal Server Error")
    void getGeneralSum_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(sumAssignmentService.getGeneralSumByIsFlat(any(Boolean.class))).thenThrow(new RuntimeException());

        //ACT & VERIFY
        mockMvc.perform(get("/auth/calculations/property/sum")
                        .param("isFlat", String.valueOf(true)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("if the sum assignment was successfully received then return status OK")
    void getSumAssignment_shouldReturnSumAssignmentDto() throws Exception {
        //ARRANGE
        when(sumAssignmentService.getSumAssignmentByIsFlatAndGeneralSum(true, BigDecimal.valueOf(100000.00)))
                .thenReturn(responseSumAssignmentDto);

        //ACT
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/calculations/property")
                        .param("isFlat", String.valueOf(true))
                        .param("generalSum", String.valueOf(BigDecimal.valueOf(100000.00)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(responseSumAssignmentDto)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseSumAssignmentDto), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the sum assignment wasn't successfully received then return Internal Server Error")
    void getSumAssignment_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(sumAssignmentService.getSumAssignmentByIsFlatAndGeneralSum(any(Boolean.class), any(BigDecimal.class)))
                .thenThrow(new RuntimeException());

        //ACT & VERIFY
        mockMvc.perform(get("/auth/calculations/property")
                        .param("isFlat", String.valueOf(true))
                        .param("generalSum", String.valueOf(BigDecimal.valueOf(100000.00))))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("if coefficients was successfully received then return status OK")
    void getCoefficients_shouldReturnCoefficientsCalculationDto() throws Exception {
        //ARRANGE
        when(factorService.getCoefficients(any(String.class), any(CategoryGroup.class), any(CapacityGroup.class),
                any(Boolean.class), any(String.class), any(DrivingExperience.class)))
                .thenReturn(coefficientsCalculationDto);
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get("/auth/calculations/car")
                        .param("region", "Москва")
                        .param("categoryGroup", CategoryGroup.TRUCK.name())
                        .param("capacityGroup", CapacityGroup.OVER_120_TO_150_INCLUSIVE.name())
                        .param("isWithInsuredAccident", String.valueOf(true))
                        .param("birthday", "1995-01-01")
                        .param("drivingExperience", DrivingExperience.TILL_5_YEARS.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(coefficientsCalculationDto)))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(asJsonString(coefficientsCalculationDto), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if coefficients wasn't successfully received then return Internal Server Error")
    void getCoefficients_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(factorService.getCoefficients(any(String.class), any(CategoryGroup.class), any(CapacityGroup.class),
                any(Boolean.class), any(String.class), any(DrivingExperience.class)))
                .thenThrow(new RuntimeException());
        //ACT & VERIFY
        mockMvc.perform(get("/auth/calculations/car")
                        .param("region", "Москва")
                        .param("categoryGroup", CategoryGroup.TRUCK.name())
                        .param("capacityGroup", CapacityGroup.OVER_120_TO_150_INCLUSIVE.name())
                        .param("isWithInsuredAccident", String.valueOf(true))
                        .param("birthday", "1995-01-01")
                        .param("drivingExperience", DrivingExperience.TILL_5_YEARS.name()))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("if finalPrice was successfully received then return status OK")
    void getFinalPrice_shouldReturnFinalPriceDto() throws Exception {
        //ARRANGE
        when(travelProgramService.getFinalPrice(
                true,
                3,
                1000,
                LocalDate.of(2022, 12, 11),
                LocalDate.of(2022, 12, 21),
                true)).thenReturn(responseFinalPriceDto);
        //ACT
        MvcResult mvcResult = mockMvc.perform(get("/auth/calculations/travel")
                        .param("isWithSportType", String.valueOf(true))
                        .param("insuredNumber", String.valueOf(3))
                        .param("basicPrice", String.valueOf(1000))
                        .param("startDate", "11122022")
                        .param("lastDate", "21122022")
                        .param("isWithPCR", String.valueOf(true)))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyBody(asJsonString(responseFinalPriceDto), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if finalPrice wasn't successfully received then return Internal Server Error")
    void getFinalPrice_ifNotSuccess_thenReturnStatus500() throws Exception{
        //ARRANGE
        when(travelProgramService.getFinalPrice(
                true,
                3,
                1000,
                LocalDate.of(2022, 12, 11),
                LocalDate.of(2022, 12, 21),
                true)).thenThrow(new RuntimeException("testErrorMessage"));

        //ACT & VERIFY
        mockMvc.perform(get("/auth/calculations/travel")
                .param("isWithSportType", String.valueOf(true))
                .param("insuredNumber", String.valueOf(3))
                .param("basicPrice", String.valueOf(1000))
                .param("startDate", "11122022")
                .param("lastDate", "21122022")
                .param("isWithPCR", String.valueOf(true)))
                .andExpect(status().isInternalServerError());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}
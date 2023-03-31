package by.afinny.insuranceservice.integration.controller;

import by.afinny.insuranceservice.config.annotation.TestWithPostgresContainer;
import by.afinny.insuranceservice.dto.CoefficientsCalculationDto;
import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;
import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DrivingExperience;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.mapper.SumAssignmentMapper;
import by.afinny.insuranceservice.repository.SumAssignmentRepository;
import by.afinny.insuranceservice.service.FactorService;
import by.afinny.insuranceservice.service.SumAssignmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestWithPostgresContainer
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Integration test for CalculationController")
public class CalculationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SumAssignmentRepository sumAssignmentRepository;

    @Autowired
    SumAssignmentMapper sumAssignmentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FactorService factorService;

    @MockBean
    private SumAssignmentService sumAssignmentService;


    private SumAssignment sumAssignment;
    private SumAssignment sumAssignment1;
    private List<SumAssignment> sumAssignmentList;
    private List<ResponseGeneralSumDto> responseGeneralSumDtoList;
    private ResponseSumAssignmentDto responseSumAssignmentDto;
    private CoefficientsCalculationDto coefficientsCalculationDto;

    @BeforeAll
    void setUp() {

        sumAssignment = SumAssignment.builder()
                .id(1)
                .isFlat(true)
                .name(ItemOfExpense.FLAT_WALLS)
                .generalSum(BigDecimal.valueOf(20.00))
                .sum(BigDecimal.valueOf(30))
                .minSum(BigDecimal.valueOf(10))
                .maxSum(BigDecimal.valueOf(40))
                .defaultSum(BigDecimal.valueOf(20))
                .build();
        sumAssignment1 = SumAssignment.builder()
                .id(2)
                .isFlat(false)
                .name(ItemOfExpense.FLAT_WALLS)
                .generalSum(BigDecimal.valueOf(40.00))
                .sum(BigDecimal.valueOf(30))
                .minSum(BigDecimal.valueOf(10))
                .maxSum(BigDecimal.valueOf(40))
                .defaultSum(BigDecimal.valueOf(20))
                .build();


    }

    @BeforeEach
    void save() {
        sumAssignmentRepository.save(sumAssignment);
        sumAssignmentRepository.save(sumAssignment1);
    }

    @ParameterizedTest
    @MethodSource("generateIsFlat")
    @DisplayName("if sumAssignment isFlat true or false then return generalSum")
    void ifSumAssigmentIsFlat_isTrueOrFalse_thenReturnResponseGeneralSum(Boolean isFlat) throws Exception {
        //ARRANGE
        sumAssignmentList = sumAssignmentRepository.findSumAssignmentsByIsFlat(isFlat);
        responseGeneralSumDtoList = sumAssignmentMapper.sumAssignmentsToGeneralSumDto(sumAssignmentList);
        when(sumAssignmentService.getGeneralSumByIsFlat(isFlat)).thenReturn(responseGeneralSumDtoList);

        //ACT
        MvcResult result = mockMvc.perform(
                        get("/auth/calculations/property/sum")
                                .param("isFlat", String.valueOf(isFlat)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseGeneralSumDtoList), result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if sumAssignment isFlat true or false not success then return Exception")
    void ifSumAssigmentIsFlat_isTrueOrFalse_ifNotSuccess_thenThrowRuntimeException() throws Exception {
        //ARRANGE
        when(sumAssignmentService.getGeneralSumByIsFlat(true)).thenThrow(new RuntimeException());

        //ACT & VERIFY
        mockMvc.perform(
                        get("/auth/calculations/property/sum")
                                .param("isFlat", "true"))
                .andExpect(status().isInternalServerError());
    }

    @ParameterizedTest
    @MethodSource("generateIsFlatAndGeneralSum")
    @DisplayName("if find sumAssignment by isFlat and generalSum success then return responseSumAssignmentDto")
    void findSumAssignmentByIsFlatAndGeneralSum_thenReturnResponseSumAssignment(Boolean isFlat, BigDecimal generalSum) throws Exception {
        //ARRANGE
        SumAssignment sumAssignment = sumAssignmentRepository.findSumAssignmentByIsFlatAndGeneralSum(isFlat, generalSum).get();
        responseSumAssignmentDto = sumAssignmentMapper.sumAssignmentToSumAssignmentDto(sumAssignment);
        when(sumAssignmentService.getSumAssignmentByIsFlatAndGeneralSum(isFlat, generalSum)).thenReturn(responseSumAssignmentDto);

        //ACT
        MvcResult mvcResult = mockMvc.perform(get("/auth/calculations/property")
                        .param("isFlat", String.valueOf(isFlat))
                        .param("generalSum", String.valueOf(generalSum)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(responseSumAssignmentDto), mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @MethodSource("generateIsFlatAndGeneralSum")
    @DisplayName("if find sumAssignment by isFlat and generalSum not success then return Exception")
    void findSumAssignmentByIsFlatAndGeneralSum_notSuccess_thenReturnException(Boolean isFlat, BigDecimal generalSum) throws Exception {
        //ARRANGE
        when(sumAssignmentService.getSumAssignmentByIsFlatAndGeneralSum(isFlat, generalSum)).thenThrow(new RuntimeException());

        //ACT & VERIFY
        mockMvc.perform(get("/auth/calculations/property")
                        .param("isFlat", String.valueOf(isFlat))
                        .param("generalSum", String.valueOf(generalSum)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void getCoefficients_whenInputParameters_regionCategoryGroupCapacityGroupIsWithInsuredAccidentBirthdayDrivingExperience() throws Exception {
        //ARRANGE
        CoefficientsCalculationDto coefficientsCalculationDto = factorService
                .getCoefficients("Москва", CategoryGroup.TRUCK, CapacityGroup.UP_TO_120_INCLUSIVE, false, String.valueOf(LocalDate.of(1993, 03, 31)), DrivingExperience.FROM_5_TO_15_YEARS);

        //ACT
        MvcResult result = mockMvc.perform(get("/auth/calculations/car")
                        .param("region", "Москва")
                        .param("categoryGroup", String.valueOf(CategoryGroup.TRUCK))
                        .param("capacityGroup", String.valueOf(CapacityGroup.UP_TO_120_INCLUSIVE))
                        .param("isWithInsuredAccident", "false")
                        .param("birthday", String.valueOf(LocalDate.of(1993, 03, 31)))
                        .param("drivingExperience", String.valueOf(DrivingExperience.FROM_5_TO_15_YEARS)))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY

        verifyBody(asJsonString(coefficientsCalculationDto), result.getResponse().getContentAsString());
    }

    private Stream<Arguments> generateIsFlat() {
        return Stream.of(
                Arguments.of(
                        Named.of("true", true)),
                Arguments.of(
                        Named.of("false", false))
        );
    }

    private Stream<Arguments> generateIsFlatAndGeneralSum() {
        return Stream.of(
                Arguments.of(
                        Named.of("isFlat", true),
                        Named.of("generalSum", BigDecimal.valueOf(20))
                ),
                Arguments.of(
                        Named.of("isFlat", false),
                        Named.of("generalSum", BigDecimal.valueOf(40))
                )
        );
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}

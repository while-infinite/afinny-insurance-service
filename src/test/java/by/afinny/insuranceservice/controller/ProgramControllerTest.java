package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.ProgramDto;
import by.afinny.insuranceservice.service.ProgramService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(ProgramController.class)
@ActiveProfiles("test")
public class ProgramControllerTest {

    @MockBean
    private ProgramService programService;

    @Autowired
    private MockMvc mockMvc;

    private List<ProgramDto> programDtoList;

    @BeforeEach
    void setUp() {
        programDtoList = List.of(ProgramDto.builder()
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
                .programId("1")
                .build());
    }

    @Test
    @DisplayName("if the list of program was successfully received then return status OK")
    void getMedicinePrograms_shouldReturnProgramDtoList() throws Exception {
        //ARRANGE
        when(programService.getMedicinePrograms(any(Integer.class), any(Integer.class), any(Boolean.class),
                any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class)))
                .thenReturn(programDtoList);

        //ACT
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/insurance-program/new-medicine/programs")
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(1))
                        .param("emergencyHospitalization", String.valueOf(true))
                        .param("dentalService", String.valueOf(true))
                        .param("telemedicine", String.valueOf(true))
                        .param("emergencyMedicalCare", String.valueOf(true))
                        .param("callingDoctor", String.valueOf(true))
                        .param("outpatientService", String.valueOf(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(programDtoList)))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        verifyBody(asJsonString(programDtoList), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the list of program wasn't successfully received then return Internal Server Error")
    void getGeneralSum_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(programService.getMedicinePrograms(any(Integer.class), any(Integer.class), any(Boolean.class),
                any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class), any(Boolean.class)))
                .thenThrow(new RuntimeException());

        //ACT & VERIFY
        mockMvc.perform(get("/auth/insurance-program/new-medicine/programs")
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(1))
                        .param("emergencyHospitalization", String.valueOf(true))
                        .param("dentalService", String.valueOf(true))
                        .param("telemedicine", String.valueOf(true))
                        .param("emergencyMedicalCare", String.valueOf(true))
                        .param("callingDoctor", String.valueOf(true))
                        .param("outpatientService", String.valueOf(true)))
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

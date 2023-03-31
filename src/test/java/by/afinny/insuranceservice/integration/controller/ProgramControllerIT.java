package by.afinny.insuranceservice.integration.controller;

import by.afinny.insuranceservice.config.annotation.TestWithPostgresContainer;
import by.afinny.insuranceservice.dto.ProgramDto;
import by.afinny.insuranceservice.entity.Program;
import by.afinny.insuranceservice.mapper.ProgramMapper;
import by.afinny.insuranceservice.repository.ProgramRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestWithPostgresContainer
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Integration test for CalculationController")
public class ProgramControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramMapper programMapper;

    private Program program;

    @BeforeAll
    void setUp(){

        program = Program.builder()
                .name("name")
                .sum(BigDecimal.valueOf(60000).movePointLeft(2))
                .organization("organization")
                .link("link")
                .description("description")
                .isEmergencyHospitalization(true)
                .isCallingDoctor(true)
                .isDentalService(true)
                .isTelemedicine(true)
                .isOutpatientService(true)
                .isEmergencyMedicalCare(true)
                .build();
    }
    @BeforeEach
    void save(){
        programRepository.save(program);
    }

    @Test
    @DisplayName("")
     void  getMedicinePrograms_success_returnProgramDto() throws Exception {
        //ARRANGE
        List<Program>programs = List.of(program);
        List<ProgramDto> list = programMapper.toProgramDtoList(programs);


        //ACT
       MvcResult result = mockMvc.perform(get("/auth/insurance-program/new-medicine/programs")
                .param("pageNumber","0")
                .param("pageSize","4")
                .param("emergencyHospitalization","true")
                .param("dentalService","true")
                .param("telemedicine","true")
                .param("emergencyMedicalCare","true")
                .param("callingDoctor","true")
                .param("outpatientService","true"))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY

        verifyBody(asJsonString(list),result.getResponse().getContentAsString());

    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).usingDefaultComparator().isEqualTo(expectedBody);
    }
}

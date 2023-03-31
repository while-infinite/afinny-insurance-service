package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Program;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/schema-h2.sql"}
)
@ActiveProfiles("test")
public class ProgramRepositoryTest {

    @Autowired
    private ProgramRepository programRepository;

    private Program program;

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void cleanUp() {
        programRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("If program exists then return the program specification")
    void getProgramSpecification_thenReturnProgramSpecification() {
        //ARRANGE
        programRepository.save(program);
        //ACT
        Specification<Program> programSpecification = programRepository.getProgramSpecification(
                program.getIsEmergencyHospitalization(), program.getIsDentalService(), program.getIsTelemedicine(),
                program.getIsEmergencyMedicalCare(), program.getIsCallingDoctor(), program.getIsOutpatientService());

        Page<Program> programs = programRepository.findAll(programSpecification, PageRequest.of(0, 1));
        List<Program> programList = programs.stream().collect(Collectors.toList());
        //VERIFY
        verifyProgram(programList.get(0), program);
    }

    private void verifyProgram(Program expected, Program actual) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId())
                    .isEqualTo(expected.getId());
            softAssertions.assertThat(actual.getName())
                    .isEqualTo(expected.getName());
            softAssertions.assertThat(actual.getSum())
                    .isEqualTo(expected.getSum());
            softAssertions.assertThat(actual.getOrganization())
                    .isEqualTo(expected.getOrganization());
            softAssertions.assertThat(actual.getLink())
                    .isEqualTo(expected.getLink());
            softAssertions.assertThat(actual.getDescription())
                    .isEqualTo(expected.getDescription());
            softAssertions.assertThat(actual.getIsEmergencyHospitalization())
                    .isEqualTo(expected.getIsEmergencyHospitalization());
            softAssertions.assertThat(actual.getIsDentalService())
                    .isEqualTo(expected.getIsDentalService());
            softAssertions.assertThat(actual.getIsTelemedicine())
                    .isEqualTo(expected.getIsTelemedicine());
            softAssertions.assertThat(actual.getIsEmergencyMedicalCare())
                    .isEqualTo(expected.getIsEmergencyMedicalCare());
            softAssertions.assertThat(actual.getIsCallingDoctor())
                    .isEqualTo(expected.getIsCallingDoctor());
            softAssertions.assertThat(actual.getIsOutpatientService())
                    .isEqualTo(expected.getIsOutpatientService());
        });
    }
}

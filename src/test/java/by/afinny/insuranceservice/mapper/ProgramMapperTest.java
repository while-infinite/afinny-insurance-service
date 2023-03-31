package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.ProgramDto;
import by.afinny.insuranceservice.entity.Program;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ProgramMapperTest {

    @InjectMocks
    private ProgramMapperImpl programMapper;

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

    @Test
    @DisplayName("verify List<ProgramDto> fields settings")
    void toProgramDtoList_shouldReturnListProgramDto() {
        List<ProgramDto> programDtoList = programMapper.toProgramDtoList(List.of(program));
        verifyProgramDto(programDtoList.get(0));
    }

    @Test
    @DisplayName("verify ProgramDto fields settings")
    void sumAssignmentToGeneralSumDto_shouldReturnResponseGeneralSumDto() {
        ProgramDto programDto = programMapper.toProgramDtoList(program);
        verifyProgramDto(programDto);
    }

    private void verifyProgramDto(ProgramDto programDto) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(programDto.getProgramId()).isEqualTo(program.getId());
            softAssertions.assertThat(programDto.getName()).isEqualTo(program.getName());
            softAssertions.assertThat(programDto.getSum()).isEqualTo(program.getSum());
            softAssertions.assertThat(programDto.getOrganization()).isEqualTo(program.getOrganization());
            softAssertions.assertThat(programDto.getLink()).isEqualTo(program.getLink());
            softAssertions.assertThat(programDto.getDescription()).isEqualTo(program.getDescription());
            softAssertions.assertThat(programDto.getIsEmergencyHospitalization()).isEqualTo(program.getIsEmergencyMedicalCare());
            softAssertions.assertThat(programDto.getIsDentalService()).isEqualTo(program.getIsDentalService());
            softAssertions.assertThat(programDto.getIsTelemedicine()).isEqualTo(program.getIsTelemedicine());
            softAssertions.assertThat(programDto.getIsEmergencyMedicalCare()).isEqualTo(program.getIsEmergencyMedicalCare());
            softAssertions.assertThat(programDto.getIsCallingDoctor()).isEqualTo(program.getIsCallingDoctor());
            softAssertions.assertThat(programDto.getIsOutpatientService()).isEqualTo(program.getIsOutpatientService());
        });
    }
}

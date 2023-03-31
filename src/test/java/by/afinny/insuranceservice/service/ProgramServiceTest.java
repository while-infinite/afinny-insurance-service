package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.ProgramDto;
import by.afinny.insuranceservice.entity.Program;
import by.afinny.insuranceservice.mapper.ProgramMapperImpl;
import by.afinny.insuranceservice.repository.ProgramRepository;
import by.afinny.insuranceservice.service.impl.ProgramServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
public class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramMapperImpl programMapper;

    @InjectMocks
    private ProgramServiceImpl programService;

    private List<ProgramDto> programDtoList;

    private Specification<Program> specification;

    private Page<Program> programs;

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

        specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
                predicates.add(builder.equal(root.get("isEmergencyHospitalization"), true));
                predicates.add(builder.equal(root.get("isDentalService"), true));
                predicates.add(builder.equal(root.get("isTelemedicine"), true));
                predicates.add(builder.equal(root.get("isEmergencyMedicalCare"), true));
                predicates.add(builder.equal(root.get("isCallingDoctor"), true));
                predicates.add(builder.equal(root.get("isOutpatientService"), true));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        programs = Page.empty(PageRequest.of(0, 1));
    }

    @Test
    @DisplayName("Return list of program when program was found.")
    void getMedicinePrograms_shouldReturnListProgramDto() {
        //ARRANGE
        when(programRepository.getProgramSpecification(any(Boolean.class), any(Boolean.class), any(Boolean.class),
                any(Boolean.class), any(Boolean.class), any(Boolean.class)))
                .thenReturn(specification);
        when(programRepository.findAll(specification, PageRequest.of(0, 1)))
                .thenReturn(programs);
        when(programMapper.toProgramDtoList(programs.stream().collect(Collectors.toList())))
                .thenReturn(programDtoList);
        //ACT
        List<ProgramDto> programDtos = programService.getMedicinePrograms(0, 1,
                true, true, true, true, true, true);
        //VERIFY
        assertThat(programDtos).isEqualTo(programDtoList);
    }

    @Test
    @DisplayName("If program wasn't found then throw exception")
    void getMedicinePrograms_ifNotSuccess_thenThrow() {
        //ARRANGE
        when(programRepository.getProgramSpecification(any(Boolean.class), any(Boolean.class), any(Boolean.class),
                any(Boolean.class), any(Boolean.class), any(Boolean.class)))
                .thenReturn(specification);
        when(programRepository.findAll(specification, PageRequest.of(0, 1)))
                .thenReturn(programs);
        when(programMapper.toProgramDtoList(programs.stream().collect(Collectors.toList())))
                .thenThrow(RuntimeException.class);
        //ACT
        ThrowableAssert.ThrowingCallable createOrderMethodInvocation = () -> programService.getMedicinePrograms(0, 1,
                true, true, true, true, true, true);
        //VERIFY
        assertThatThrownBy(createOrderMethodInvocation).isInstanceOf(RuntimeException.class);
    }
}

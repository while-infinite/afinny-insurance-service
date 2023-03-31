package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;
import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import by.afinny.insuranceservice.mapper.SumAssignmentMapperImpl;
import by.afinny.insuranceservice.repository.SumAssignmentRepository;
import by.afinny.insuranceservice.service.impl.SumAssignmentServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
public class SumAssignmentServiceTest {

    @Mock
    private SumAssignmentRepository sumAssignmentRepository;

    @Mock
    private SumAssignmentMapperImpl sumAssignmentMapper;

    @InjectMocks
    private SumAssignmentServiceImpl sumAssignmentService;

    private List<SumAssignment> sumAssignmentsWithIsFlatTrue;

    private List<SumAssignment> getSumAssignmentsWithIsFlatFalse;

    private List<ResponseGeneralSumDto> generalSumWithIsFlatTrueDto;

    private List<ResponseGeneralSumDto> generalSumWithIsFlatFalseDto;

    private SumAssignment sumAssignment;

    private ResponseSumAssignmentDto responseSumAssignmentDto;


    @BeforeEach
    void setUp() {
        sumAssignment = SumAssignment.builder()
                .id(1)
                .isFlat(true)
                .name(ItemOfExpense.FLAT_FURNITURE)
                .generalSum(new BigDecimal("100000.00"))
                .sum(new BigDecimal("99000.00"))
                .minSum(new BigDecimal("90000.00"))
                .maxSum(new BigDecimal("110000.00"))
                .defaultSum(new BigDecimal("101000.00"))
                .build();

        sumAssignmentsWithIsFlatTrue = List.of(sumAssignment,
                SumAssignment.builder()
                        .id(2)
                        .isFlat(true)
                        .name(ItemOfExpense.FLAT_REPAIR)
                        .generalSum(new BigDecimal("70000.00"))
                        .sum(new BigDecimal("65000.00"))
                        .minSum(new BigDecimal("60000.00"))
                        .maxSum(new BigDecimal("80000.00"))
                        .defaultSum(new BigDecimal("73000.00"))
                        .build());

        getSumAssignmentsWithIsFlatFalse = List.of(SumAssignment.builder()
                .id(3)
                .isFlat(false)
                .name(ItemOfExpense.HOUSE_NEIGHBOURS)
                .generalSum(new BigDecimal("55000.00"))
                .sum(new BigDecimal("50000.00"))
                .minSum(new BigDecimal("45000.00"))
                .maxSum(new BigDecimal("60000.00"))
                .defaultSum(new BigDecimal("57000.00"))
                .build());

        generalSumWithIsFlatTrueDto = List.of(ResponseGeneralSumDto.builder()
                        .generalSum("100000.00").build(),
                ResponseGeneralSumDto.builder()
                        .generalSum("70000.00").build());

        generalSumWithIsFlatFalseDto = List.of(ResponseGeneralSumDto.builder()
                .generalSum("55000.00").build());

        responseSumAssignmentDto = ResponseSumAssignmentDto.builder()
                .name(String.valueOf(ItemOfExpense.FLAT_FURNITURE))
                .minSum(String.valueOf(new BigDecimal("90000.00")))
                .maxSum(String.valueOf(new BigDecimal("110000.00")))
                .defaultSum(String.valueOf(new BigDecimal("101000.00")))
                .build();
    }

    @Test
    @DisplayName("Return list of general sum by isFlat when exists")
    void getGeneralSumByIsFlat_shouldReturnListResponseGeneralSumDto() {
        //ARRANGE
        when(sumAssignmentRepository.findSumAssignmentsByIsFlat(true))
                .thenReturn(sumAssignmentsWithIsFlatTrue);
        when(sumAssignmentRepository.findSumAssignmentsByIsFlat(false))
                .thenReturn(getSumAssignmentsWithIsFlatFalse);
        when(sumAssignmentMapper.sumAssignmentsToGeneralSumDto(sumAssignmentsWithIsFlatTrue))
                .thenReturn(generalSumWithIsFlatTrueDto);
        when(sumAssignmentMapper.sumAssignmentsToGeneralSumDto(getSumAssignmentsWithIsFlatFalse))
                .thenReturn(generalSumWithIsFlatFalseDto);
        //ACT
        List<ResponseGeneralSumDto> expectedGeneralSumWithIsFlatTrueDtoList = sumAssignmentService.getGeneralSumByIsFlat(true);
        List<ResponseGeneralSumDto> expectedGeneralSumWithIsFlatFalseDtoList = sumAssignmentService.getGeneralSumByIsFlat(false);
        //VERIFY
        assertThat(expectedGeneralSumWithIsFlatTrueDtoList).isEqualTo(generalSumWithIsFlatTrueDto);
        assertThat(expectedGeneralSumWithIsFlatFalseDtoList).isEqualTo(generalSumWithIsFlatFalseDto);
    }

    @Test
    @DisplayName("Return sum assignment by isFlat and generalSum if SumAssignment was found.")
    void getSumAssignmentByIsFlatAndGeneralSum_shouldReturnResponseSumAssignmentDto() {
        //ARRANGE
        when(sumAssignmentRepository.findSumAssignmentByIsFlatAndGeneralSum(any(Boolean.class), any(BigDecimal.class)))
                .thenReturn(Optional.of(sumAssignment));
        when(sumAssignmentMapper.sumAssignmentToSumAssignmentDto(sumAssignment))
                .thenReturn(responseSumAssignmentDto);
        //ACT
        ResponseSumAssignmentDto sumAssignmentDto = sumAssignmentService
                .getSumAssignmentByIsFlatAndGeneralSum(true, BigDecimal.valueOf(100000.00));
        //VERIFY
        assertThat(sumAssignmentDto).isEqualTo(responseSumAssignmentDto);
    }

    @Test
    @DisplayName("If SumAssignment with these isFlat and generalSum wasn't found then throw EntityNotFoundException")
    void getSumAssignmentByIsFlatAndGeneralSum_ifSumAssignmentNotFound_thenThrow() {
        //ARRANGE
        when(sumAssignmentRepository.findSumAssignmentByIsFlatAndGeneralSum(any(Boolean.class), any(BigDecimal.class)))
                .thenReturn(Optional.empty());
        //ACT
        ThrowableAssert.ThrowingCallable createOrderMethodInvocation = () -> sumAssignmentService
                .getSumAssignmentByIsFlatAndGeneralSum(true, BigDecimal.valueOf(100000.00));
        //VERIFY
        assertThatThrownBy(createOrderMethodInvocation).isInstanceOf(EntityNotFoundException.class);
    }
}

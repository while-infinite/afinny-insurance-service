package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;
import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
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
class SumAssignmentMapperTest {

    @InjectMocks
    private SumAssignmentMapperImpl sumAssignmentMapper;

    private SumAssignment sumAssignment;

    @BeforeEach
    void setUp() {
        sumAssignment = SumAssignment.builder()
                .isFlat(true)
                .name(ItemOfExpense.FLAT_FURNITURE)
                .generalSum(new BigDecimal("100000.00"))
                .sum(new BigDecimal("99000.00"))
                .minSum(new BigDecimal("90000.00"))
                .maxSum(new BigDecimal("110000.00"))
                .defaultSum(new BigDecimal("101000.00"))
                .build();
    }

    @Test
    @DisplayName("verify List<ResponseGeneralSumDto> fields settings")
    void sumAssignmentsToGeneralSumDto_shouldReturnListResponseGeneralSumDto() {
        List<ResponseGeneralSumDto> responseGeneralSumDtoList = sumAssignmentMapper.sumAssignmentsToGeneralSumDto(List.of(sumAssignment));
        verifyResponseGeneralSumDto(responseGeneralSumDtoList.get(0));
    }

    @Test
    @DisplayName("verify ResponseGeneralSumDto fields settings")
    void sumAssignmentToGeneralSumDto_shouldReturnResponseGeneralSumDto() {
        ResponseGeneralSumDto responseGeneralSumDto = sumAssignmentMapper.sumAssignmentToGeneralSumDto(sumAssignment);
        verifyResponseGeneralSumDto(responseGeneralSumDto);
    }

    @Test
    @DisplayName("verify ResponseSumAssignmentDto fields settings")
    void sumAssignmentToSumAssignmentDto_shouldReturnResponseUserPolicyDto() {
        ResponseSumAssignmentDto responseSumAssignmentDto = sumAssignmentMapper.sumAssignmentToSumAssignmentDto(sumAssignment);
        verifyResponseSumAssignmentDto(responseSumAssignmentDto);
    }

    private void verifyResponseGeneralSumDto(ResponseGeneralSumDto generalSumDto) {
        assertSoftly(softAssertions -> softAssertions.assertThat(generalSumDto.getGeneralSum())
                .isEqualTo(sumAssignment.getGeneralSum().toString()));
    }

    private void verifyResponseSumAssignmentDto(ResponseSumAssignmentDto sumAssignmentDto) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(sumAssignmentDto.getId()).isEqualTo(sumAssignment.getId());
            softAssertions.assertThat(sumAssignmentDto.getName()).isEqualTo(sumAssignment.getName().toString());
            softAssertions.assertThat(sumAssignmentDto.getMinSum()).isEqualTo(sumAssignment.getMinSum().toString());
            softAssertions.assertThat(sumAssignmentDto.getMaxSum()).isEqualTo(sumAssignment.getMaxSum().toString());
            softAssertions.assertThat(sumAssignmentDto.getDefaultSum()).isEqualTo(sumAssignment.getDefaultSum().toString());
        });
    }
}
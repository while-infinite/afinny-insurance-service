package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.entity.constant.ItemOfExpense;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/schema-h2.sql"}
)
@ActiveProfiles("test")
class SumAssignmentRepositoryTest {

    @Autowired
    private SumAssignmentRepository sumAssignmentRepository;

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

    @AfterEach
    void cleanUp() {
        sumAssignmentRepository.deleteAll();
    }

    @Test
    @DisplayName("If SumAssignment with this isFlat exists then return the sumAssignmentList")
    void findSumAssignmentsByIsFlat_thenReturnSumAssignmentList() {
        //ARRANGE
        sumAssignmentRepository.save(sumAssignment);
        //ACT
        List<SumAssignment> sumAssignmentListWithIsFlatTrue = sumAssignmentRepository.findSumAssignmentsByIsFlat(true);
        List<SumAssignment> sumAssignmentListWithIsFlatFalse = sumAssignmentRepository.findSumAssignmentsByIsFlat(false);
        //VERIFY
        assertThat(sumAssignmentListWithIsFlatTrue).hasSize(1);
        assertThat(sumAssignmentListWithIsFlatTrue.isEmpty()).isEqualTo(false);
        assertThat(sumAssignmentListWithIsFlatFalse.isEmpty()).isEqualTo(true);
        verifySumAssignment(sumAssignment ,sumAssignmentListWithIsFlatTrue.get(0));
    }

    @Test
    @DisplayName("If SumAssignment with these isFlat and generalSum exists then return the SumAssignment")
    void findSumAssignmentByIsFlatAndGeneralSum_thenReturnSumAssignment() {
        //ARRANGE
        sumAssignmentRepository.save(sumAssignment);
        //ACT
        SumAssignment sumAssignmentWithParameters = sumAssignmentRepository
                .findSumAssignmentByIsFlatAndGeneralSum(true, BigDecimal.valueOf(100000.00))
                .orElseThrow(() -> new EntityNotFoundException("SumAssignment with these parameters was not found"));
        //VERIFY
        verifySumAssignment(sumAssignment, sumAssignmentWithParameters);
    }

    @Test
    @DisplayName("If SumAssignment with these isFlat and generalSum doesn't exists then return empty")
    void findSumAssignmentByIsFlatAndGeneralSum_IfSumAssignmentNotExists_thenReturnEmpty() {
        //ACT
        Optional<SumAssignment> sumAssignmentWithParameters = sumAssignmentRepository
                .findSumAssignmentByIsFlatAndGeneralSum(false, BigDecimal.valueOf(2314123.00));
        //VERIFY
        assertThat(sumAssignmentWithParameters.isEmpty()).isTrue();
    }

    private void verifySumAssignment(SumAssignment expected, SumAssignment actual) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(expected.getId()).isEqualTo(actual.getId());
            softAssertions.assertThat(expected.getIsFlat()).isEqualTo(actual.getIsFlat());
            softAssertions.assertThat(expected.getName()).isEqualTo(actual.getName());
            softAssertions.assertThat(expected.getGeneralSum()).isEqualTo(actual.getGeneralSum());
            softAssertions.assertThat(expected.getSum()).isEqualTo(actual.getSum());
            softAssertions.assertThat(expected.getMinSum()).isEqualTo(actual.getMinSum());
            softAssertions.assertThat(expected.getMaxSum()).isEqualTo(actual.getMaxSum());
            softAssertions.assertThat(expected.getDefaultSum()).isEqualTo(actual.getDefaultSum());
        });
    }
}
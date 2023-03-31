package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.CoefficientsCalculationDto;
import by.afinny.insuranceservice.dto.FactorDto;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DrivingExperience;
import by.afinny.insuranceservice.service.impl.FactorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
public class FactorServiceTest {

    private final FactorService factorService = new FactorServiceImpl();

    private CoefficientsCalculationDto coefficientsCalculationDto;

    @BeforeEach
    void setUp() {
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
    }

    @ParameterizedTest
    @MethodSource("generateArgs")
    @DisplayName("Return coefficients depending on the input parameters")
    void getCoefficients_shouldReturnCoefficientsCalculationDto(String region, CategoryGroup categoryGroup, CapacityGroup capacityGroup,
                                                                Boolean isWithInsuredAccident, String birthday,
                                                                DrivingExperience drivingExperience) {
        CoefficientsCalculationDto actual = factorService.getCoefficients(region, categoryGroup, capacityGroup,
                isWithInsuredAccident, birthday, drivingExperience);

        verifyCoefficients(coefficientsCalculationDto, actual);
    }

    private static Stream<Arguments> generateArgs() {
        return Stream.of(Arguments.of("Москва", CategoryGroup.TRUCK, CapacityGroup.OVER_120_TO_150_INCLUSIVE,
                true, "1970-01-01", DrivingExperience.TILL_5_YEARS));
    }

    private void verifyCoefficients(CoefficientsCalculationDto expected, CoefficientsCalculationDto actual) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(expected.getBaseRate()).isEqualTo(actual.getBaseRate());
            expected.getFactors().forEach(x -> {
                softAssertions.assertThat(x.getFactorName()).isEqualTo(actual.getFactors().get(expected.getFactors().indexOf(x)).getFactorName());
                softAssertions.assertThat(x.getFactorRate()).isEqualTo(actual.getFactors().get(expected.getFactors().indexOf(x)).getFactorRate());
            });
        });
    }
}

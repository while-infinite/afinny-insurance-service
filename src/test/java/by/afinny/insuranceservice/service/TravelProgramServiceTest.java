package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.ResponseFinalPriceDto;
import by.afinny.insuranceservice.service.impl.TravelProgramServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

@ActiveProfiles("test")
public class TravelProgramServiceTest {

    private final TravelProgramService travelProgramService = new TravelProgramServiceImpl();

    private ResponseFinalPriceDto expectedResponseFinalPriceDto;

    @BeforeEach
    void setUp() {
        expectedResponseFinalPriceDto = ResponseFinalPriceDto.builder()
                .finalPrice(61990)
                .build();
    }

    @Test
    @DisplayName("Return finalPrice by parameters")
    void getFinalPrice_shouldReturnFinalPriceDto() {
        ResponseFinalPriceDto actualResponseFinalPriceDto = travelProgramService.getFinalPrice(
                true,
                3,
                1000,
                LocalDate.of(2022, 12, 11),
                LocalDate.of(2022, 12, 21),
                true);
        assertThat(actualResponseFinalPriceDto.getFinalPrice()).isEqualTo(expectedResponseFinalPriceDto.getFinalPrice());
    }
}
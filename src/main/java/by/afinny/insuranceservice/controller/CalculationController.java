package by.afinny.insuranceservice.controller;

import by.afinny.insuranceservice.dto.CoefficientsCalculationDto;
import by.afinny.insuranceservice.dto.ResponseFinalPriceDto;
import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DrivingExperience;
import by.afinny.insuranceservice.service.FactorService;
import by.afinny.insuranceservice.service.SumAssignmentService;
import by.afinny.insuranceservice.service.TravelProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/calculations")
public class CalculationController {

    private final SumAssignmentService sumAssignmentService;
    private final FactorService factorService;
    private final TravelProgramService travelProgramService;

    @GetMapping("/property/sum")
    public ResponseEntity<List<ResponseGeneralSumDto>> getGeneralSum(@RequestParam Boolean isFlat) {
        List<ResponseGeneralSumDto> responseGeneralSumDtoList = sumAssignmentService.getGeneralSumByIsFlat(isFlat);
        return ResponseEntity.ok(responseGeneralSumDtoList);
    }

    @GetMapping("/property")
    public ResponseEntity<ResponseSumAssignmentDto> getSumAssignment(@RequestParam Boolean isFlat,
                                                                     @RequestParam BigDecimal generalSum) {
        ResponseSumAssignmentDto responseSumAssignmentDto = sumAssignmentService.getSumAssignmentByIsFlatAndGeneralSum(isFlat, generalSum);
        return ResponseEntity.ok(responseSumAssignmentDto);
    }

    @GetMapping("/car")
    public ResponseEntity<CoefficientsCalculationDto> getCoefficients(@RequestParam String region,
                                                                      @RequestParam CategoryGroup categoryGroup,
                                                                      @RequestParam CapacityGroup capacityGroup,
                                                                      @RequestParam Boolean isWithInsuredAccident,
                                                                      @RequestParam String birthday,
                                                                      @RequestParam DrivingExperience drivingExperience) {
        CoefficientsCalculationDto coefficientsCalculationDto = factorService
                .getCoefficients(region, categoryGroup, capacityGroup, isWithInsuredAccident, birthday, drivingExperience);
        return ResponseEntity.ok(coefficientsCalculationDto);
    }

    @GetMapping("/travel")
    public ResponseEntity<ResponseFinalPriceDto> getFinalPrice(@RequestParam Boolean isWithSportType,
                                                               @RequestParam Integer insuredNumber,
                                                               @RequestParam Integer basicPrice,
                                                               @RequestParam @DateTimeFormat(pattern = "ddMMyyyy") LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(pattern = "ddMMyyyy") LocalDate lastDate,
                                                               @RequestParam Boolean isWithPCR) {
        ResponseFinalPriceDto responseFinalPriceDto = travelProgramService
                .getFinalPrice(isWithSportType,insuredNumber,basicPrice, startDate, lastDate, isWithPCR);
        return ResponseEntity.ok(responseFinalPriceDto);
    }
}

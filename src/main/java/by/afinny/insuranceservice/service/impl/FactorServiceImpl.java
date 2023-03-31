package by.afinny.insuranceservice.service.impl;

import by.afinny.insuranceservice.dto.CoefficientsCalculationDto;
import by.afinny.insuranceservice.dto.FactorDto;
import by.afinny.insuranceservice.entity.constant.BaseRate;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DrivingExperience;
import by.afinny.insuranceservice.entity.constant.FactorName;
import by.afinny.insuranceservice.entity.constant.FactorRate;
import by.afinny.insuranceservice.service.FactorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FactorServiceImpl implements FactorService {

    CoefficientsCalculationDto coefficientsCalculationDto = new CoefficientsCalculationDto();
    List<FactorDto> factorDtoList = new ArrayList<>();

    @Override
    public CoefficientsCalculationDto getCoefficients(String region, CategoryGroup categoryGroup, CapacityGroup capacityGroup,
                                                      Boolean isWithInsuredAccident, String birthday,
                                                      DrivingExperience drivingExperience) {

        setBaseRateByCategoryGroup(categoryGroup, coefficientsCalculationDto);

        setFactorRateByRegion(region, coefficientsCalculationDto, factorDtoList);

        setFactorRateByCapacityGroup(capacityGroup, coefficientsCalculationDto, factorDtoList);

        if (!isWithInsuredAccident) {
            setFields(FactorName.LIGHT, FactorRate.K1, factorDtoList, coefficientsCalculationDto);
        }

        setFactorRateByUserAge(birthday, coefficientsCalculationDto, factorDtoList);

        setFactorRateByDrivingExperience(drivingExperience, coefficientsCalculationDto, factorDtoList);

        return coefficientsCalculationDto;
    }

    private void setBaseRateByCategoryGroup(CategoryGroup categoryGroup, CoefficientsCalculationDto coefficientsCalculationDto) {
        switch (categoryGroup) {
            case PASSENGER_AUTOMOBILE:
                coefficientsCalculationDto.setBaseRate(BaseRate.RUB_5000.getRate());
                break;
            case TRUCK:
                coefficientsCalculationDto.setBaseRate(BaseRate.RUB_7000.getRate());
                break;
        }
    }

    private void setFactorRateByRegion(String region, CoefficientsCalculationDto coefficientsCalculationDto, List<FactorDto> factorDtoList) {
        switch (region) {
            case "Москва":
                setFields(FactorName.HARD, FactorRate.K3, factorDtoList, coefficientsCalculationDto);
                break;
            case "Санкт-Петербург":
                setFields(FactorName.MIDDLE, FactorRate.K2, factorDtoList, coefficientsCalculationDto);
                break;
            default:
                setFields(FactorName.LIGHT, FactorRate.K1, factorDtoList, coefficientsCalculationDto);
                break;
        }
    }

    private void setFactorRateByCapacityGroup(CapacityGroup capacityGroup, CoefficientsCalculationDto coefficientsCalculationDto, List<FactorDto> factorDtoList) {
        switch (capacityGroup) {
            case UP_TO_120_INCLUSIVE:
                setFields(FactorName.LIGHT, FactorRate.K1, factorDtoList, coefficientsCalculationDto);
                break;
            case OVER_120_TO_150_INCLUSIVE:
                setFields(FactorName.MIDDLE, FactorRate.K2, factorDtoList, coefficientsCalculationDto);
                break;
            case OVER_150:
                setFields(FactorName.HARD, FactorRate.K3, factorDtoList, coefficientsCalculationDto);
                break;
        }
    }

    private void setFactorRateByDrivingExperience(DrivingExperience drivingExperience, CoefficientsCalculationDto coefficientsCalculationDto, List<FactorDto> factorDtoList) {
        switch (drivingExperience) {
            case TILL_5_YEARS:
                setFields(FactorName.HARD, FactorRate.K3, factorDtoList, coefficientsCalculationDto);
                break;
            case FROM_5_TO_15_YEARS:
                setFields(FactorName.MIDDLE, FactorRate.K2, factorDtoList, coefficientsCalculationDto);
                break;
            case MORE_15_YEARS:
                setFields(FactorName.LIGHT, FactorRate.K1, factorDtoList, coefficientsCalculationDto);
                break;
        }
    }

    private void setFactorRateByUserAge(String birthday, CoefficientsCalculationDto coefficientsCalculationDto, List<FactorDto> factorDtoList) {
        switch (getFactorNameByUserBirthday(birthday)) {
            case HARD:
                setFields(FactorName.HARD, FactorRate.K3, factorDtoList, coefficientsCalculationDto);
                break;
            case MIDDLE:
                setFields(FactorName.MIDDLE, FactorRate.K2, factorDtoList, coefficientsCalculationDto);
                break;
            case LIGHT:
                setFields(FactorName.LIGHT, FactorRate.K1, factorDtoList, coefficientsCalculationDto);
                break;
        }
    }

    private FactorName getFactorNameByUserBirthday(String birthday) {
        LocalDate userBirthday = LocalDate.parse(birthday);

        int years = Period.between(userBirthday, LocalDate.now()).getYears();

        if (years >= 18 && years <= 35) {
            return FactorName.HARD;
        } else if (years > 35 && years <= 50) {
            return FactorName.MIDDLE;
        } else {
            return FactorName.LIGHT;
        }
    }

    private void setFields(FactorName factorName, FactorRate factorRate,
                           List<FactorDto> factorDtoList,
                           CoefficientsCalculationDto coefficientsCalculationDto) {
        FactorDto factorDto = new FactorDto();
        factorDto.setFactorName(factorName.name());
        factorDto.setFactorRate(factorRate.getRate());
        factorDtoList.add(factorDto);
        coefficientsCalculationDto.setFactors(factorDtoList);
    }
}

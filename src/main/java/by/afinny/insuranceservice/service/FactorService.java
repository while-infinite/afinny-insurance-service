package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.CoefficientsCalculationDto;
import by.afinny.insuranceservice.entity.constant.CapacityGroup;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import by.afinny.insuranceservice.entity.constant.DrivingExperience;

public interface FactorService {
    CoefficientsCalculationDto getCoefficients(String region, CategoryGroup categoryGroup, CapacityGroup capacityGroup,
                                               Boolean isWithInsuredAccident, String birthday, DrivingExperience drivingExperience);
}

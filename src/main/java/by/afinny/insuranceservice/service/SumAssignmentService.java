package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;

import java.math.BigDecimal;
import java.util.List;

public interface SumAssignmentService {

    List<ResponseGeneralSumDto> getGeneralSumByIsFlat(Boolean isFlat);

    ResponseSumAssignmentDto getSumAssignmentByIsFlatAndGeneralSum(Boolean isFlat, BigDecimal generalSum);
}
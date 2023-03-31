package by.afinny.insuranceservice.service.impl;

import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;
import by.afinny.insuranceservice.entity.SumAssignment;
import by.afinny.insuranceservice.mapper.SumAssignmentMapper;
import by.afinny.insuranceservice.repository.SumAssignmentRepository;
import by.afinny.insuranceservice.service.SumAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SumAssignmentServiceImpl implements SumAssignmentService {

    private final SumAssignmentRepository sumAssignmentRepository;

    private final SumAssignmentMapper sumAssignmentMapper;

    @Override
    public List<ResponseGeneralSumDto> getGeneralSumByIsFlat(Boolean isFlat) {
        log.info("getGeneralSumByIsFlat() method invoke");
        List<SumAssignment> sumAssignments = sumAssignmentRepository.findSumAssignmentsByIsFlat(isFlat);
        return sumAssignmentMapper.sumAssignmentsToGeneralSumDto(sumAssignments);
    }

    @Override
    public ResponseSumAssignmentDto getSumAssignmentByIsFlatAndGeneralSum(Boolean isFlat, BigDecimal generalSum) {
        log.info("getSumAssignmentByIsFlatAndGeneralSum() method invoke");
        SumAssignment sumAssignment = sumAssignmentRepository.findSumAssignmentByIsFlatAndGeneralSum(isFlat, generalSum)
                .orElseThrow(() -> new EntityNotFoundException("SumAssignment with these parameters was not found"));
        return sumAssignmentMapper.sumAssignmentToSumAssignmentDto(sumAssignment);
    }
}
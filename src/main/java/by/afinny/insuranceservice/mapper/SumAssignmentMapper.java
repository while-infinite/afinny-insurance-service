package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.ResponseGeneralSumDto;
import by.afinny.insuranceservice.dto.ResponseSumAssignmentDto;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.entity.SumAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface SumAssignmentMapper {

    List<ResponseGeneralSumDto> sumAssignmentsToGeneralSumDto(List<SumAssignment> sumAssignments);

    ResponseGeneralSumDto sumAssignmentToGeneralSumDto(SumAssignment sumAssignment);

    ResponseSumAssignmentDto sumAssignmentToSumAssignmentDto(SumAssignment sumAssignment);

    @Mapping(source = "sumAssignmentSum",target = "sum")
    @Mapping(source = "sumAssignmentName",target = "name")
    @Mapping(source = "sumAssignmentGeneralSum",target = "generalSum")
    @Mapping(source = "sumAssignmentMinSum",target = "minSum")
    @Mapping(source = "sumAssignmentMaxSum",target = "maxSum")
    @Mapping(source = "sumAssignmentDefaultSum",target = "defaultSum")
    SumAssignment toSumAssignmentFromEvent(ConsumerNewRealEstatePolicy consumerNewRealEstatePolicy);
}

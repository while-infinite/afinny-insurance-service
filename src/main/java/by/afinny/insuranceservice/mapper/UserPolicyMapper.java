package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserPolicyMapper {

    List<ResponseUserPolicyDto> applicationsToUserPolicyDto(List<Application> applications);

    @Mapping(source = "application.id", target = "applicationId")
    @Mapping(source = "application.insuranceType", target = "insuranceType")
    @Mapping(source = "application.registrationDate", target = "registrationDate")
    @Mapping(source = "application.insuranceStatus", target = "insuranceStatus")
    @Mapping(source = "application.agreement.number", target = "number")
    @Mapping(source = "application.agreement.agreementDate", target = "agreementDate")
    ResponseUserPolicyDto applicationToUserPolicyDto(Application application);
}
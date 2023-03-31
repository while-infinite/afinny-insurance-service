package by.afinny.insuranceservice.mapper;

import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponseNewMedicinePolicyDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewMedicineEvent;
import by.afinny.insuranceservice.dto.kafka.ProducerNewMedicineEvent;
import by.afinny.insuranceservice.entity.Agreement;
import by.afinny.insuranceservice.dto.ResponseTravelPolicyDto;
import by.afinny.insuranceservice.entity.Application;
import by.afinny.insuranceservice.entity.Insurant;
import by.afinny.insuranceservice.entity.Insured;
import by.afinny.insuranceservice.entity.MedicalInsurance;
import by.afinny.insuranceservice.entity.Person;
import by.afinny.insuranceservice.entity.Program;
import by.afinny.insuranceservice.entity.TravelInsurance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ApplicationMapper {
    List<ResponseApplicationInsuranceTypeDto> toResponseTypes(List<Application> allByInsuranceType);

    ResponseRejectionLetterDto toResponseLetter(Application orElseThrow);

    Insured toInsured(RequestMedicinePolicyDto requestMedicinePolicyDto);

    Person toPerson(RequestMedicinePolicyDto requestMedicinePolicyDto);

    Program toProgram(RequestMedicinePolicyDto requestMedicinePolicyDto);

    Application toApplication(RequestMedicinePolicyDto requestMedicinePolicyDto);

    Insurant toInsurant(RequestMedicinePolicyDto requestMedicinePolicyDto);

    @Mapping(source = "programId", target = "program.id")
    MedicalInsurance toMedicalInsurance(RequestMedicinePolicyDto requestMedicinePolicyDto);

    ResponsePaymentDetailsDto toResponsePaymentDetails(Application application);

    @Mapping(source = "application.id", target = "applicationId")
    ResponseNewMedicinePolicyDto toResponseNewMedicinePolicyDto(Application application, Person person, Insurant insurant, Insured insured);

    ProducerNewMedicineEvent toProducerNewMedicineEvent(UUID id, ResponseNewMedicinePolicyDto responseNewMedicinePolicyDto);

    @Mapping(source = "consumerNewMedicineEvent.applicationId", target = "id")
    Agreement toAgreement(ConsumerNewMedicineEvent consumerNewMedicineEvent);

    Insured toInsured(RequestTravelPolicyDto requestTravelPolicyDto);

    Person toPerson(RequestTravelPolicyDto requestTravelPolicyDto);

    Application toApplication(RequestTravelPolicyDto requestTravelPolicyDto);

    Insurant toInsurant(RequestTravelPolicyDto requestTravelPolicyDto);

    @Mapping(source = "travelProgramId", target = "travelProgram.id")
    TravelInsurance toTravelInsurance(RequestTravelPolicyDto requestTravelPolicyDto);

    @Mapping(source = "application.id", target = "applicationId")
    @Mapping(source = "travelInsurance.travelProgram.id", target = "travelProgramId")
    ResponseTravelPolicyDto toResponseTravelPolicyDto(Application application, Insured insured, Insurant insurant,
                                                      Person person, TravelInsurance travelInsurance);
}
package by.afinny.insuranceservice.service;


import by.afinny.insuranceservice.dto.RequestMedicinePolicyDto;
import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import by.afinny.insuranceservice.dto.ResponseApplicationInsuranceTypeDto;
import by.afinny.insuranceservice.dto.ResponseNewMedicinePolicyDto;
import by.afinny.insuranceservice.dto.ResponsePaymentDetailsDto;
import by.afinny.insuranceservice.dto.ResponseRejectionLetterDto;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewMedicineEvent;
import by.afinny.insuranceservice.dto.ResponseTravelPolicyDto;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {

    List<ResponseApplicationInsuranceTypeDto> getInsuranceTypes();

    ResponseRejectionLetterDto getRejectionLetter(UUID clientId, UUID applicationId);

    ResponseNewMedicinePolicyDto registerNewMedicinePolicy(RequestMedicinePolicyDto requestMedicinePolicyDto);

    ResponsePaymentDetailsDto getPaymentDetails(UUID clientId, UUID applicationId);

    void deleteApplication(UUID clientId, UUID applicationId);

    void saveNewMedicinePolicy(ConsumerNewMedicineEvent consumerNewMedicineEvent);

    ResponseTravelPolicyDto createNewTravelPolicy(RequestTravelPolicyDto travelPolicyDto);
}
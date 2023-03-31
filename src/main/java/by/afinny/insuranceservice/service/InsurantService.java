package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.PolicyInfoDto;
import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.ResponseUserPolicyDto;
import by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent;

import java.util.List;
import java.util.UUID;

public interface InsurantService {

    List<ResponseUserPolicyDto> getAllUserPolicies(String clientId);

    PolicyInfoDto getPolicyInformation(UUID clientId, UUID applicationId);

    void createNewPolicy(RequestNewPolicy requestNewPolicy);

    void saveNewPolicy(ConsumerNewPolicyEvent event);

    void createNewRealEstatePolicy(RequestNewRealEstatePolicy requestNewRealEstatePolicy);

    void saveNewRealEstatePolicy (ConsumerNewRealEstatePolicy consumerNewRealEstatePolicy);
}
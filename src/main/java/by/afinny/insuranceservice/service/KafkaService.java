package by.afinny.insuranceservice.service;

import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.kafka.ProducerNewPolicyEvent;

public interface KafkaService {

    void sendToKafka(String id, RequestNewPolicy requestNewPolicy);

    void sendToKafka (RequestNewRealEstatePolicy requestNewRealEstatePolicy);
}

package by.afinny.insuranceservice.service.impl;

import by.afinny.insuranceservice.dto.RequestNewPolicy;
import by.afinny.insuranceservice.dto.RequestNewRealEstatePolicy;
import by.afinny.insuranceservice.dto.kafka.ProducerNewPolicyEvent;
import by.afinny.insuranceservice.dto.kafka.ProducerNewRealEstatePolicy;
import by.afinny.insuranceservice.mapper.PolicyMapper;
import by.afinny.insuranceservice.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    private final PolicyMapper policyMapper;
    private final ApplicationEventPublisher eventPublisher;

    public void sendToKafka(String id, RequestNewPolicy requestNewPolicy) {
        ProducerNewPolicyEvent event = policyMapper.toProducerNewPolicyEvent(id, requestNewPolicy);
        log.info("Publishing event: RequestNewPolicy");
        eventPublisher.publishEvent(event);
    }

    public void sendToKafka(RequestNewRealEstatePolicy requestNewRealEstatePolicy) {
        log.info("Publishing event: RequestNewRealEstatePolicy");
        ProducerNewRealEstatePolicy event = policyMapper.toProducerNewRealEstatePolicy(requestNewRealEstatePolicy);
        eventPublisher.publishEvent(event);
    }

}

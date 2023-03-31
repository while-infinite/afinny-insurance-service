package by.afinny.insuranceservice.integration;

import by.afinny.insuranceservice.dto.RequestTravelPolicyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewTravelPolicySource {

    private final KafkaTemplate<String, ?> kafkaTemplate;

    @Value("${kafka.topics.insurance-travel-producer.path}")
    private String topic;

    @EventListener
    public void sendMessageAboutNewTravelPolicy(RequestTravelPolicyDto requestTravelPolicyDto) {
        log.info("sendMessageAboutNewTravelPolicy() method invoke");
        log.info("Dto " + requestTravelPolicyDto + " has been received, sending message...");
        kafkaTemplate.send(
                MessageBuilder
                        .withPayload(requestTravelPolicyDto)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build()
        );
    }
}

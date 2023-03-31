package by.afinny.insuranceservice.config.kafka;

import by.afinny.insuranceservice.config.kafka.properties.KafkaConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaConfigProperties.class)
public class KafkaConsumerConfig {

    private final KafkaConfigProperties config;
    private KafkaProperties kafkaProperties;
    private String BOOTSTRAP_SERVERS;

    @PostConstruct
    private void createKafkaProperties() {
        kafkaProperties = config.getKafkaProperties();
        BOOTSTRAP_SERVERS = config.getBootstrapServers();
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> stubConsumerFactoryNewPolicy() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.insuranceservice.dto.kafka.ProducerNewPolicyEvent");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "stubKafkaListenerNewPolicy")
    public ConcurrentKafkaListenerContainerFactory<String, Object> stubFactoryNewPolicy() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stubConsumerFactoryNewPolicy());

        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> consumerFactoryNewPolicy() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.insuranceservice.dto.kafka.ConsumerNewPolicyEvent");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "kafkaListenerNewPolicy")
    public ConcurrentKafkaListenerContainerFactory<String, Object> factoryNewPolicy() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryNewPolicy());

        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> consumerFactoryNewMedicinePolicy() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.insuranceservice.dto.kafka.ConsumerNewMedicineEvent");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "kafkaListenerNewMedicinePolicy")
    public ConcurrentKafkaListenerContainerFactory<String, Object> factoryNewMedicinePolicy() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryNewMedicinePolicy());

        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> stubConsumerFactoryNewMedicinePolicy() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.insuranceservice.dto.kafka.ProducerNewMedicineEvent");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "stubKafkaListenerNewMedicinePolicy")
    public ConcurrentKafkaListenerContainerFactory<String, Object> stubFactoryNewMedicinePolicy() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stubConsumerFactoryNewMedicinePolicy());

        return factory;
    }



    @Bean
    public DefaultKafkaConsumerFactory<String, Object> stubConsumerFactoryNewRealEstatePolicy() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.insuranceservice.dto.kafka.ProducerNewRealEstatePolicy");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "stubKafkaListenerNewRealEstatePolicy")
    public ConcurrentKafkaListenerContainerFactory<String, Object> stubFactoryNewRealEstatePolicy() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stubConsumerFactoryNewRealEstatePolicy());

        return factory;
    }

    @Bean
    public DefaultKafkaConsumerFactory<String, Object> consumerFactoryNewRealEstatePolicy() {
        Map<String, Object> properties = getKafkaConsumerProperties();
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "by.afinny.insuranceservice.dto.kafka.ConsumerNewRealEstatePolicy");
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "kafkaListenerNewRealEstatePolicy")
    public ConcurrentKafkaListenerContainerFactory<String, Object> factoryNewRealEstatePolicy() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryNewRealEstatePolicy());

        return factory;
    }

    private Map<String, Object> getKafkaConsumerProperties() {
        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerProperties.get("key.deserializer"));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerProperties.get("value.deserializer"));
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, "false");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);

        return props;
    }
}

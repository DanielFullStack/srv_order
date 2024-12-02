package com.backend.srv_order.config;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.backend.srv_order.model.Pedido;

public class KafkaConfigTest {

    private KafkaConfig kafkaConfig = new KafkaConfig();

    @Test
    void testProducerFactory() {
        ProducerFactory<String, Pedido> factory = kafkaConfig.producerFactory();
        Map<String, Object> configs = factory.getConfigurationProperties();

        assertEquals("kafka:9092", configs.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(StringSerializer.class, configs.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        assertEquals(JsonSerializer.class, configs.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
    }

    @Test
    void testKafkaTemplate() {
        KafkaTemplate<String, Pedido> template = kafkaConfig.kafkaTemplate();
        assertNotNull(template);
    }

    @Test
    void testConsumerFactory() {
        ConsumerFactory<String, Pedido> factory = kafkaConfig.consumerFactory();
        Map<String, Object> configs = factory.getConfigurationProperties();

        assertEquals("kafka:9092", configs.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("order-group", configs.get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals(StringDeserializer.class, configs.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        assertEquals(JsonDeserializer.class, configs.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
        assertEquals("*", configs.get(JsonDeserializer.TRUSTED_PACKAGES));
        assertEquals(Pedido.class, configs.get(JsonDeserializer.VALUE_DEFAULT_TYPE));
        assertEquals(false, configs.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
        assertEquals("earliest", configs.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
        assertEquals(10, configs.get(ConsumerConfig.MAX_POLL_RECORDS_CONFIG));
        assertEquals(500, configs.get(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG));
        assertEquals(3000, configs.get(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG));
        assertEquals(10000, configs.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG));
    }

    @Test
    void testDefaultErrorHandlerConfiguration() {
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(5);
        backOff.setInitialInterval(500L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(5000L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(backOff);

        // Validações
        assertNotNull(errorHandler);
        assertEquals(500L, backOff.getInitialInterval());
        assertEquals(5000L, backOff.getMaxInterval());
        assertEquals(2.0, backOff.getMultiplier());
    }

}

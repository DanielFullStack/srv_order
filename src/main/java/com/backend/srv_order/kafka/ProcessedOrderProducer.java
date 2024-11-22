package com.backend.srv_order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;

@Service
public class ProcessedOrderProducer {
    private static final String PROCESSED_TOPIC = "processed-orders";

    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    public void enviarPedidoProcessado(Pedido pedido) {
        kafkaTemplate.send(PROCESSED_TOPIC, pedido);
    }
}


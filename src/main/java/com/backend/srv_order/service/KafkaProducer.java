package com.backend.srv_order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;

@Service
public class KafkaProducer {
    private static final String TOPIC = "orders";

    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    public void enviarPedido(Pedido pedido) {
        kafkaTemplate.send(TOPIC, pedido);
    }
}


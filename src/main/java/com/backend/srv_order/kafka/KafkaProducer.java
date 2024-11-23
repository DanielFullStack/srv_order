package com.backend.srv_order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;

@Service
public class KafkaProducer {    
    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    public void enviarPedido(String topico, Pedido pedido) {
        kafkaTemplate.send(topico, pedido);
    }
}


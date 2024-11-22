package com.backend.srv_order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;

@Service
public class KafkaConsumer {
    @Autowired
    private PedidoService pedidoService;

    @KafkaListener(topics = "orders", groupId = "order-group")
    public void consumirPedido(Pedido pedido) {
        System.out.println("Recebendo pedido do Kafka: " + pedido);
        pedidoService.processarPedido(pedido);
    }
}


package com.backend.srv_order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.PedidoService;

@Service
public class KafkaConsumer {
    @Autowired
    private PedidoService pedidoService;

    @KafkaListener(topics = "orders", groupId = "order-group")
    public void consumirPedido(Pedido pedido) {
        pedidoService.processarPedido(pedido);
    }
}


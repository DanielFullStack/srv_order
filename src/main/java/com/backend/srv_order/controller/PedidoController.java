package com.backend.srv_order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.KafkaProducer;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestBody Pedido pedido) {
        kafkaProducer.enviarPedido(pedido);
        return ResponseEntity.ok("Pedido enviado ao Kafka com sucesso!");
    }
}
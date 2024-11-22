package com.backend.srv_order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.srv_order.kafka.KafkaConsumer;
import com.backend.srv_order.kafka.KafkaProducer;
import com.backend.srv_order.model.Pedido;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class PedidoController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Operation(summary = "Criar novo pedido", description = "Envia um novo pedido para processamento via Kafka")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido enviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Pedido inválido"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestBody Pedido pedido) {
        kafkaProducer.enviarPedido(pedido);
        return ResponseEntity.ok("Pedido enviado ao Kafka com sucesso!");
    }

    @Operation(summary = "Consumir pedido", description = "Consome um pedido do Kafka para processamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido consumido com sucesso"),
        @ApiResponse(responseCode = "400", description = "Pedido inválido"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/consumir")
    public ResponseEntity<String> consumirPedido(@RequestBody Pedido pedido) {
        kafkaConsumer.consumirPedido(pedido);
        return ResponseEntity.ok("Pedido consumido com sucesso!");
    }
}
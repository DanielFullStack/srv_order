package com.backend.srv_order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Criar novo pedido", description = "Cria e envia um novo pedido para processamento via Kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido enviado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Pedido inválido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoService.enviarPedido(pedido);
        return ResponseEntity.ok("Pedido criado com sucesso! ID: " + novoPedido.getId());
    }

    @Operation(summary = "Processar pedido", description = "Processa um pedido previamente enviado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido processado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Pedido inválido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/processar")
    public ResponseEntity<String> processarPedido(@RequestBody Pedido pedido) {
        Pedido pedidoProcessado = pedidoService.processarPedido(pedido);
        return ResponseEntity.ok("Pedido processado com sucesso! ID: " + pedidoProcessado.getId());
    }
}

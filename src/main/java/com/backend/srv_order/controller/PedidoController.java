package com.backend.srv_order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    // Endpoint para receber pedidos do Produto Externo A
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Pedido pedidoProcessado = pedidoService.processarPedido(pedido);
        return ResponseEntity.ok(pedidoProcessado);
    }

    // Endpoint para consultar pedidos pelo Produto Externo B
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos(@RequestParam(required = false) String status) {
        List<Pedido> pedidos = pedidoService.buscarPedidosPorStatus(status != null ? status : "PROCESSADO");
        return ResponseEntity.ok(pedidos);
    }
}


package com.backend.srv_order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.repository.PedidoRepository;
import com.backend.srv_order.kafka.KafkaProducer;

@Service
public class PedidoService {

    private static final String PEDIDO_PROCESSADO_TOPIC = "pedido-processado-topic";
    private static final String PEDIDO_CANCELADO_TOPIC = "pedido-cancelado-topic";

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public void salvarPedido(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedidoPorId(Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.orElse(null);
    }

    // Processa o pedido por ID e envia para outro tópico
    public Pedido processarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        kafkaProducer.enviarPedido(PEDIDO_PROCESSADO_TOPIC, pedido);
        return pedido;
    }

    // Cancela o pedido por ID e envia para outro tópico
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        kafkaProducer.enviarPedido(PEDIDO_CANCELADO_TOPIC, pedido);
        return pedido;
    }
}
package com.backend.srv_order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.srv_order.kafka.KafkaProducer;
import com.backend.srv_order.kafka.ProcessedOrderProducer;
import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.model.Produto;
import com.backend.srv_order.repository.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ProcessedOrderProducer processedOrderProducer;

    public Pedido enviarPedido(Pedido pedido) {
        // Salva o pedido inicial no banco
        pedido.setStatus("CRIADO");
        Pedido novoPedido = pedidoRepository.save(pedido);

        // Envia o pedido para o tópico Kafka
        kafkaProducer.enviarPedido(novoPedido);
        return novoPedido;
    }

    public Pedido processarPedido(Pedido pedido) {
        // Calcula o valor total do pedido
        double valorTotal = pedido.getProdutos()
                .stream()
                .mapToDouble(Produto::getPreco)
                .sum();
        pedido.setValorTotal(valorTotal);
        pedido.setStatus("PROCESSADO");

        // Salva o pedido processado no banco
        Pedido pedidoProcessado = pedidoRepository.save(pedido);

        // Envia o pedido processado ao Kafka
        processedOrderProducer.enviarPedidoProcessado(pedidoProcessado);

        return pedidoProcessado;
    }
}

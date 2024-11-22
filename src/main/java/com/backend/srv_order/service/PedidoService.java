package com.backend.srv_order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.model.Produto;
import com.backend.srv_order.repository.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProcessedOrderProducer processedOrderProducer;

    public Pedido processarPedido(Pedido pedido) {
        // Calcula o valor total do pedido
        double valorTotal = pedido.getProdutos()
                .stream()
                .mapToDouble(Produto::getPreco)
                .sum();
        pedido.setValorTotal(valorTotal);
        pedido.setStatus("PROCESSADO");

        // Salva no banco de dados
        Pedido pedidoProcessado = pedidoRepository.save(pedido);

        // Envia o pedido processado ao Kafka
        processedOrderProducer.enviarPedidoProcessado(pedidoProcessado);

        return pedidoProcessado;
    }
}

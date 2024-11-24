package com.backend.srv_order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Item;
import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.model.Produto;
import com.backend.srv_order.repository.PedidoRepository;
import com.backend.srv_order.repository.ProdutoRepository;
import com.backend.srv_order.enums.KafkaPedidoTopicEnum;
import com.backend.srv_order.enums.PedidoStatusEnum;
import com.backend.srv_order.kafka.KafkaProducer;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public void salvarPedido(Pedido pedido) {
        // Verifica duplicidade de pedido baseado no banco de dados
        for (Item item : pedido.getItens()) {
            Produto produto = item.getProduto();
            if (produto.getId() == null) {
                produto = produtoRepository.save(produto);
                item.setProduto(produto);
            } else {
                boolean isDuplicado = pedidoRepository.existsByProdutoIdAndQuantidade(produto.getId(),
                        item.getQuantidade());
                if (isDuplicado) {
                    throw new RuntimeException("Pedido duplicado detectado com o produto ID: " + produto.getId());
                }
            }
        }

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
        double valorTotal = pedido.getItens()
                .stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(PedidoStatusEnum.PROCESSADO.name());
        pedidoRepository.save(pedido);
        kafkaProducer.enviarPedido(KafkaPedidoTopicEnum.PEDIDO_PROCESSADO_TOPIC.getTopic(), pedido);
        return pedido;
    }

    // Cancela o pedido por ID e envia para outro tópico
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        pedido.setStatus(PedidoStatusEnum.CANCELADO.name());
        pedidoRepository.save(pedido);
        kafkaProducer.enviarPedido(KafkaPedidoTopicEnum.PEDIDO_CANCELADO_TOPIC.getTopic(), pedido);
        return pedido;
    }
}
package com.backend.srv_order.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public void salvarPedido(Pedido pedido) {
        logger.info("Iniciando salvamento do pedido");

        // Valida itens antes de salvar
        validarItensPedido(pedido);

        // Calcula o valor total
        double valorTotal = pedido.getItens()
                .stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
        pedido.setValorTotal(valorTotal);

        // Salva o pedido com os itens
        pedidoRepository.save(pedido);
        logger.info("Pedido salvo com sucesso com ID: {}", pedido.getId());
    }

    private void validarItensPedido(Pedido pedido) {
        boolean pedidoDuplicado = verificarPedidoDuplicado(pedido);

        if (pedidoDuplicado) {
            logger.error("Pedido duplicado detectado: {}", pedido);
            throw new RuntimeException("Pedido duplicado detectado");
        }

        for (Item item : pedido.getItens()) {
            Produto produto = item.getProduto();

            Produto produtoFinal = produtoRepository.findByProdutoNomeAndProdutoPreco(produto.getNome(),
                    produto.getPreco());

            if (produtoFinal == null) {
                logger.debug("Produto não encontrado, salvando novo produto: {}", produto);
                produtoFinal = produtoRepository.save(produto);
            }

            item.setProduto(produtoFinal);
            item.setPedido(pedido);
        }
    }

    private boolean verificarPedidoDuplicado(Pedido pedido) {
        int itemCount = pedido.getItens().size();
        String status = pedido.getStatus();

        Boolean pedidoDuplicado = pedidoRepository.existsPedidoWithSameStructure(status, itemCount);
        return pedidoDuplicado != null && pedidoDuplicado;
    }

    public List<Pedido> listarPedidos() {
        logger.info("Listando todos os pedidos");
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedidoPorId(Long id) {
        logger.info("Buscando pedido por ID: {}", id);
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.orElse(null);
    }

    // Processa o pedido por ID e envia para outro tópico
    public Pedido processarPedido(Long id) {
        logger.info("Iniciando processamento do pedido ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado com ID: {}", id);
                    return new RuntimeException("Pedido não encontrado com ID: " + id);
                });
        double valorTotal = pedido.getItens()
                .stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(PedidoStatusEnum.PROCESSADO.name());
        pedidoRepository.save(pedido);
        logger.info("Enviando pedido processado para o tópico Kafka");
        kafkaProducer.enviarPedido(KafkaPedidoTopicEnum.PEDIDO_PROCESSADO_TOPIC.getTopic(), pedido);
        return pedido;
    }

    // Cancela o pedido por ID e envia para outro tópico
    public Pedido cancelarPedido(Long id) {
        logger.info("Iniciando cancelamento do pedido ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado com ID: {}", id);
                    return new RuntimeException("Pedido não encontrado com ID: " + id);
                });
        pedido.setStatus(PedidoStatusEnum.CANCELADO.name());
        pedidoRepository.save(pedido);
        logger.info("Enviando pedido cancelado para o tópico Kafka");
        kafkaProducer.enviarPedido(KafkaPedidoTopicEnum.PEDIDO_CANCELADO_TOPIC.getTopic(), pedido);
        return pedido;
    }
}
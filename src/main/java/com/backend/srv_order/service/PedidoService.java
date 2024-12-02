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

        // Realiza a validação antes de qualquer persistência
        validarDuplicidadePedido(pedido);
        validarItensPedido(pedido);

        // Calcula o valor total
        double valorTotal = pedido.getItens().stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
        pedido.setValorTotal(valorTotal);

        // Vetifica itens do pedido
        logger.info("Pedido com itens: {}", pedido.getItens());

        // Salva o pedido no banco
        processarPedido(pedido);
        logger.info("Pedido salvo com sucesso com ID: {}", pedido.getId());
    }

    private void validarItensPedido(Pedido pedido) {
        // Validar e associar os produtos aos itens
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

    private void validarDuplicidadePedido(Pedido pedido) {
        // Recupera todos os pedidos com o mesmo status do banco
        List<Pedido> pedidosExistentes = pedidoRepository.findByStatus(pedido.getStatus());
    
        // Formata a lista de itens do pedido atual para o padrão "nome-preco-quantidade"
        List<String> itensAtuais = pedido.getItens().stream()
                .map(item -> {
                    Produto produto = item.getProduto();
                    return produto.getNome() + "-" + produto.getPreco() + "-" + item.getQuantidade();
                })
                .toList();
    
        // Itera sobre os pedidos existentes para verificar duplicidade
        for (Pedido pedidoExistente : pedidosExistentes) {
            // Valida se o número de itens é igual
            if (pedidoExistente.getItens().size() != pedido.getItens().size()) {
                continue;
            }
    
            // Formata a lista de itens do pedido existente
            List<String> itensExistentes = pedidoExistente.getItens().stream()
                    .map(item -> {
                        Produto produto = item.getProduto();
                        return produto.getNome() + "-" + produto.getPreco() + "-" + item.getQuantidade();
                    })
                    .toList();
    
            // Verifica se todos os itens são iguais
            if (itensAtuais.containsAll(itensExistentes) && itensExistentes.containsAll(itensAtuais)) {
                logger.error("Pedido duplicado detectado com os seguintes itens: {}", itensAtuais);
                throw new RuntimeException("Pedido duplicado detectado. Estrutura: " + itensAtuais);
            }
        }
    
        logger.info("Nenhum pedido duplicado detectado.");
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
    public Pedido processarPedido(Pedido pedido) {
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
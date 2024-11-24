package com.backend.srv_order.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.PedidoService;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private PedidoService pedidoService;

    /**
     * Listener para processar mensagens em tempo real.
     */
    @KafkaListener(topics = "pedido-topic", groupId = "order-group")
    public void consumirPedido(Pedido pedido) {
        try {
            if (pedido != null) {
                pedidoService.salvarPedido(pedido);
                logger.info("Pedido consumido pelo listener e salvo: {}", pedido);
            } else {
                logger.warn("Mensagem consumida do Kafka era nula.");
            }
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem no listener Kafka: {}", pedido, e);
        }
    }
}


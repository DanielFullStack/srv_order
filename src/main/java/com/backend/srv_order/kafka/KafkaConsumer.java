package com.backend.srv_order.kafka;

import java.time.Duration;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import com.backend.srv_order.enums.KafkaPedidoTopicEnum;
import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.PedidoService;

import jakarta.annotation.PostConstruct;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private ConsumerFactory<String, Pedido> consumerFactory;

    @Autowired
    private PedidoService pedidoService;

    /**
     * Método executado na inicialização da aplicação para consumir mensagens desde o início.
     */
    @PostConstruct
    public void consumirMensagensDesdeInicio() {
        var consumer = consumerFactory.createConsumer();
        try {
            String topic = KafkaPedidoTopicEnum.PEDIDO_TOPIC.getTopic();
            consumer.subscribe(Collections.singletonList(topic));
            consumer.poll(Duration.ZERO); // Força o Kafka a buscar as partições atribuídas
            consumer.seekToBeginning(consumer.assignment());

            logger.info("Consumindo mensagens do tópico '{}' desde o início...", topic);

            while (true) {
                var registros = consumer.poll(Duration.ofSeconds(5));
                if (registros.isEmpty()) {
                    logger.info("Não há mais mensagens antigas para processar.");
                    break;
                }

                registros.forEach(record -> {
                    try {
                        Pedido pedido = record.value();
                        if (pedido != null) {
                            pedidoService.salvarPedido(pedido);
                            logger.info("Pedido consumido no startup e salvo: {}", pedido);
                        }
                    } catch (Exception e) {
                        logger.error("Erro ao processar registro no startup: {}", record, e);
                    }
                });

                // Confirma os offsets processados
                consumer.commitSync();
            }
        } catch (Exception e) {
            logger.error("Erro ao consumir mensagens no startup", e);
        } finally {
            consumer.close();
        }
    }

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


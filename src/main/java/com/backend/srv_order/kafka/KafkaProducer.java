package com.backend.srv_order.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;

@Service
public class KafkaProducer {    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    
    @Autowired
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    public void enviarPedido(String topico, Pedido pedido) {
        logger.info("Enviando pedido para o tópico: {} - Pedido: {}", topico, pedido);
        kafkaTemplate.send(topico, pedido);
        logger.info("Pedido enviado com sucesso para o tópico: {}", topico);
    }
}
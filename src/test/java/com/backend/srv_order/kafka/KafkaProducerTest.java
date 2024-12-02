package com.backend.srv_order.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.backend.srv_order.model.Pedido;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    @Test
    void deveEnviarPedidoComSucesso() {
        String topico = "pedidos";
        Pedido pedido = new Pedido();
        when(kafkaTemplate.send(topico, pedido)).thenReturn(null);

        kafkaProducer.enviarPedido(topico, pedido);

        verify(kafkaTemplate, times(1)).send(topico, pedido);
    }
}

package com.backend.srv_order.kafka;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.PedidoService;

public class KafkaConsumerTest {
    
    @Mock
    private PedidoService pedidoService;
    
    @InjectMocks
    private KafkaConsumer kafkaConsumer;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testConsumirPedidoComSucesso() {
        Pedido pedido = new Pedido();
        doNothing().when(pedidoService).salvarPedido(any(Pedido.class));
        
        kafkaConsumer.consumirPedido(pedido);
        
        verify(pedidoService, times(1)).salvarPedido(pedido);
    }
    
    @Test
    void testConsumirPedidoNulo() {
        kafkaConsumer.consumirPedido(null);
        
        verify(pedidoService, never()).salvarPedido(any());
    }
    
    @Test
    void testConsumirPedidoComErro() {
        Pedido pedido = new Pedido();
        doThrow(new RuntimeException("Erro ao salvar")).when(pedidoService).salvarPedido(any(Pedido.class));
        
        kafkaConsumer.consumirPedido(pedido);
        
        verify(pedidoService, times(1)).salvarPedido(pedido);
    }
}
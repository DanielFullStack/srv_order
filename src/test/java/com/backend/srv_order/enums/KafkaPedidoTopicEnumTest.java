package com.backend.srv_order.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KafkaPedidoTopicEnumTest {
    
    @Test
    public void testPedidoTopicValue() {
        assertEquals("pedido-topic", KafkaPedidoTopicEnum.PEDIDO_TOPIC.getTopic());
    }
    
    @Test
    public void testPedidoProcessadoTopicValue() {
        assertEquals("pedido-processado-topic", KafkaPedidoTopicEnum.PEDIDO_PROCESSADO_TOPIC.getTopic());
    }
    
    @Test
    public void testPedidoCanceladoTopicValue() {
        assertEquals("pedido-cancelado-topic", KafkaPedidoTopicEnum.PEDIDO_CANCELADO_TOPIC.getTopic());
    }
    
    @Test
    public void testEnumValues() {
        KafkaPedidoTopicEnum[] values = KafkaPedidoTopicEnum.values();
        assertEquals(3, values.length);
        assertTrue(containsEnumValue(values, KafkaPedidoTopicEnum.PEDIDO_TOPIC));
        assertTrue(containsEnumValue(values, KafkaPedidoTopicEnum.PEDIDO_PROCESSADO_TOPIC));
        assertTrue(containsEnumValue(values, KafkaPedidoTopicEnum.PEDIDO_CANCELADO_TOPIC));
    }
    
    private boolean containsEnumValue(KafkaPedidoTopicEnum[] values, KafkaPedidoTopicEnum value) {
        for (KafkaPedidoTopicEnum enumValue : values) {
            if (enumValue == value) {
                return true;
            }
        }
        return false;
    }
}

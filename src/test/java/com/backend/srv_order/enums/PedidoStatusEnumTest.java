package com.backend.srv_order.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoStatusEnumTest {
    
    @Test
    public void testEnumValues() {
        assertEquals(3, PedidoStatusEnum.values().length);
        assertEquals(PedidoStatusEnum.RECEBIDO, PedidoStatusEnum.valueOf("RECEBIDO"));
        assertEquals(PedidoStatusEnum.PROCESSADO, PedidoStatusEnum.valueOf("PROCESSADO"));
        assertEquals(PedidoStatusEnum.CANCELADO, PedidoStatusEnum.valueOf("CANCELADO"));
    }
    
    @Test
    public void testEnumOrder() {
        PedidoStatusEnum[] expectedOrder = {
            PedidoStatusEnum.RECEBIDO,
            PedidoStatusEnum.PROCESSADO,
            PedidoStatusEnum.CANCELADO
        };
        assertArrayEquals(expectedOrder, PedidoStatusEnum.values());
    }
    
    @Test
    public void testEnumToString() {
        assertEquals("RECEBIDO", PedidoStatusEnum.RECEBIDO.toString());
        assertEquals("PROCESSADO", PedidoStatusEnum.PROCESSADO.toString());
        assertEquals("CANCELADO", PedidoStatusEnum.CANCELADO.toString());
    }
}
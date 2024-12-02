package com.backend.srv_order.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.service.PedidoService;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarPedidos_DeveRetornarListaDePedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        List<Pedido> pedidosEsperados = Arrays.asList(pedido1, pedido2);
        when(pedidoService.listarPedidos()).thenReturn(pedidosEsperados);

        ResponseEntity<List<Pedido>> response = pedidoController.listarPedidos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidosEsperados, response.getBody());
        verify(pedidoService, times(1)).listarPedidos();
    }

    @Test
    void buscarPedidoPorId_QuandoPedidoExiste_DeveRetornarPedido() {
        Long id = 1L;
        Pedido pedidoEsperado = new Pedido();
        when(pedidoService.buscarPedidoPorId(id)).thenReturn(pedidoEsperado);

        ResponseEntity<Pedido> response = pedidoController.buscarPedidoPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoEsperado, response.getBody());
        verify(pedidoService, times(1)).buscarPedidoPorId(id);
    }

    @Test
    void buscarPedidoPorId_QuandoPedidoNaoExiste_DeveRetornarNotFound() {
        Long id = 1L;
        when(pedidoService.buscarPedidoPorId(id)).thenReturn(null);

        ResponseEntity<Pedido> response = pedidoController.buscarPedidoPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(pedidoService, times(1)).buscarPedidoPorId(id);
    }

    @Test
    void cancelarPedido_DeveRetornarPedidoCancelado() {
        Long id = 1L;
        Pedido pedidoCancelado = new Pedido();
        when(pedidoService.cancelarPedido(id)).thenReturn(pedidoCancelado);

        ResponseEntity<Pedido> response = pedidoController.cancelarPedido(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidoCancelado, response.getBody());
        verify(pedidoService, times(1)).cancelarPedido(id);
    }
}

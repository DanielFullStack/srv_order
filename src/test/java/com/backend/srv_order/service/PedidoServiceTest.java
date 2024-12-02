package com.backend.srv_order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.srv_order.model.Item;
import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.model.Produto;
import com.backend.srv_order.repository.PedidoRepository;
import com.backend.srv_order.repository.ProdutoRepository;
import com.backend.srv_order.kafka.KafkaProducer;
import com.backend.srv_order.enums.PedidoStatusEnum;

public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvarPedido() {
        Produto produto = new Produto();
        produto.setNome("Produto Test");
        produto.setPreco(10.0);

        Item item = new Item();
        item.setProduto(produto);
        item.setQuantidade(2);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(PedidoStatusEnum.RECEBIDO.name());
        pedido.setItens(Arrays.asList(item));

        when(produtoRepository.findByProdutoNomeAndProdutoPreco(anyString(), anyDouble())).thenReturn(produto);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoRepository.findByStatus(anyString())).thenReturn(Arrays.asList());
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));

        pedidoService.salvarPedido(pedido);

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        assertEquals(20.0, pedido.getValorTotal());
    }

    @Test
    void testListarPedidos() {
        List<Pedido> pedidos = Arrays.asList(new Pedido(), new Pedido());
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> result = pedidoService.listarPedidos();

        assertEquals(2, result.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPedidoPorId() {
        Pedido pedido = new Pedido();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido result = pedidoService.buscarPedidoPorId(1L);

        assertNotNull(result);
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void testProcessarPedido() {
        Produto produto = new Produto();
        produto.setNome("Produto Test");
        produto.setPreco(10.0);

        Item item = new Item();
        item.setProduto(produto);
        item.setQuantidade(2);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(PedidoStatusEnum.RECEBIDO.name());
        pedido.setItens(Arrays.asList(item));

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.processarPedido(pedido);

        assertEquals(PedidoStatusEnum.PROCESSADO.name(), result.getStatus());
        verify(kafkaProducer, times(1)).enviarPedido(anyString(), any(Pedido.class));
    }

    @Test
    void testCancelarPedido() {
        Pedido pedido = new Pedido();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido result = pedidoService.cancelarPedido(1L);

        assertEquals(PedidoStatusEnum.CANCELADO.name(), result.getStatus());
        verify(kafkaProducer, times(1)).enviarPedido(anyString(), any(Pedido.class));
    }

    @Test
    void testValidarDuplicidadePedido() {
        Produto produto = new Produto();
        produto.setNome("Produto Test");
        produto.setPreco(10.0);

        Item item = new Item();
        item.setProduto(produto);
        item.setQuantidade(2);

        Pedido pedido = new Pedido();
        pedido.setStatus(PedidoStatusEnum.RECEBIDO.name());
        pedido.setItens(Arrays.asList(item));

        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setStatus(PedidoStatusEnum.RECEBIDO.name());
        pedidoExistente.setItens(Arrays.asList(item));

        when(pedidoRepository.findByStatus(anyString())).thenReturn(Arrays.asList(pedidoExistente));

        assertThrows(RuntimeException.class, () -> pedidoService.salvarPedido(pedido));
    }
}
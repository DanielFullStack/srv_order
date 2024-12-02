package com.backend.srv_order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.backend.srv_order.model.Pedido;

@DataJpaTest
public class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    public void shouldFindPedidosByStatus() {
        Pedido pedido1 = new Pedido();
        pedido1.setStatus("PENDING");

        Pedido pedido2 = new Pedido();
        pedido2.setStatus("PENDING");

        Pedido pedido3 = new Pedido();
        pedido3.setStatus("COMPLETED");

        pedidoRepository.saveAll(List.of(pedido1, pedido2, pedido3));

        List<Pedido> foundPedidos = pedidoRepository.findByStatus("PENDING");

        assertThat(foundPedidos).hasSize(2);
        assertThat(foundPedidos).extracting(Pedido::getStatus)
                .containsOnly("PENDING");
    }

    @Test
    public void shouldReturnEmptyListWhenStatusNotFound() {
        List<Pedido> foundPedidos = pedidoRepository.findByStatus("NONEXISTENT");

        assertThat(foundPedidos).isEmpty();
    }
}
package com.backend.srv_order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.srv_order.model.Pedido;
import com.backend.srv_order.model.Produto;
import com.backend.srv_order.repository.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido processarPedido(Pedido pedido) {
        // Calcula o valor total do pedido
        double valorTotal = pedido.getProdutos()
                                  .stream()
                                  .mapToDouble(Produto::getPreco)
                                  .sum();
        pedido.setValorTotal(valorTotal);
        pedido.setStatus("PROCESSADO");
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> buscarPedidosPorStatus(String status) {
        return pedidoRepository.findByStatus(status);
    }
}


package com.backend.srv_order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.srv_order.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatus(String status);

    @Query("SELECT COUNT(p) > 0 FROM Pedido p JOIN p.itens i WHERE i.produto.id = :produtoId AND i.quantidade = :quantidade")
    boolean existsByProdutoIdAndQuantidade(@Param("produtoId") Long produtoId, @Param("quantidade") Integer quantidade);
}

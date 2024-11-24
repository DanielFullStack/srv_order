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

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Pedido p " +
            "WHERE p.status = :status " +
            "AND EXISTS (" +
            "  SELECT 1 FROM Item i1 JOIN p.itens i2 " +
            "  WHERE i1.produto.id = i2.produto.id " +
            "  AND i1.quantidade = i2.quantidade " +
            "  AND p.id = i2.pedido.id " +
            "  GROUP BY p.id " +
            "  HAVING COUNT(i1) = :itemCount " +
            ")")
    Boolean existsPedidoWithSameStructure(@Param("status") String status, @Param("itemCount") int itemCount);

}

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
            "JOIN p.itens i " +
            "GROUP BY p.id " +
            "HAVING COUNT(i) = :itemCount " +
            "   AND SUM(CASE WHEN CONCAT(i.produto.id, '-', i.quantidade) IN :itens THEN 1 ELSE 0 END) = :itemCount")
    Boolean existsPedidoWithSameStructure(@Param("itemCount") int itemCount, @Param("itens") List<String> itens);

}

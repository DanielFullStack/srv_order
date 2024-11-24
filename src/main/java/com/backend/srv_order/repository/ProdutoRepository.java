package com.backend.srv_order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.srv_order.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.nome = :produtoNome AND p.preco = :produtoPreco")
    Produto findByProdutoNomeAndProdutoPreco(@Param("produtoNome") String produtoNome, @Param("produtoPreco") Double produtoPreco);

}
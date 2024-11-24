package com.backend.srv_order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.srv_order.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}

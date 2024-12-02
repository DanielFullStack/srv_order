package com.backend.srv_order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.backend.srv_order.model.Produto;

@DataJpaTest
public class ProdutoRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Test
    public void shouldFindProdutoByNomeAndPreco() {
        Produto produto = new Produto();
        produto.setNome("Test Product");
        produto.setPreco(10.0);
        entityManager.persist(produto);
        entityManager.flush();
        
        Produto found = produtoRepository.findByProdutoNomeAndProdutoPreco("Test Product", 10.0);
        
        assertThat(found.getNome()).isEqualTo(produto.getNome());
        assertThat(found.getPreco()).isEqualTo(produto.getPreco());
    }
    
    @Test
    public void shouldReturnNullWhenProdutoNotFound() {
        Produto found = produtoRepository.findByProdutoNomeAndProdutoPreco("Nonexistent", 0.0);
        
        assertThat(found).isNull();
    }
}

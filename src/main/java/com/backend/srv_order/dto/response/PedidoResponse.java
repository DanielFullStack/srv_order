package com.backend.srv_order.dto.response;

import lombok.Data;

@Data
public class PedidoResponse {
    private Long id;
    private String mensagem;

    public PedidoResponse(Long id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }
}
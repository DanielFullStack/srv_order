package com.backend.srv_order.dto.request;

import lombok.Data;

@Data
public class PedidoRequest {
    private String produto;
    private int quantidade;
}
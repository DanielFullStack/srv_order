package com.backend.srv_order.enums;

public enum KafkaPedidoTopicEnum {
    PEDIDO_TOPIC("pedido-topic"),
    PEDIDO_PROCESSADO_TOPIC("pedido-processado-topic"),
    PEDIDO_CANCELADO_TOPIC("pedido-cancelado-topic");

    private final String topic;

    KafkaPedidoTopicEnum(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}

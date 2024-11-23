#!/bin/bash

KAFKA_BROKER=${KAFKA_BROKER:-kafka:9092}
TOPICS=("pedido-topic" "pedido-processado-topic" "pedido-cancelado-topic")

# Função para verificar se o Kafka está pronto
function wait_for_kafka() {
  echo "Esperando o Kafka inicializar em $KAFKA_BROKER..."
  while ! nc -z kafka 9092; do
    sleep 2
    echo "Ainda esperando o Kafka..."
  done
  echo "Kafka está pronto!"
}

# Criar tópicos no Kafka
function create_topics() {
  for TOPIC in "${TOPICS[@]}"; do
    echo "Criando tópico: $TOPIC"
    kafka-topics --create \
      --bootstrap-server $KAFKA_BROKER \
      --replication-factor 1 \
      --partitions 1 \
      --topic $TOPIC \
      || echo "Tópico $TOPIC já existe."
  done
}

# Enviar mensagens para o tópico de pedidos
function populate_topic() {
  echo "Populando tópico 'pedido-topic' com mensagens de exemplo..."
  kafka-console-producer --bootstrap-server $KAFKA_BROKER --topic pedido-topic <<EOF
{"produtos":[{"nome":"Notebook","preco":5000.0},{"nome":"Mouse","preco":200.0}],"status":"RECEBIDO","valorTotal":5200.0}
{"produtos":[{"nome":"Teclado","preco":300.0},{"nome":"Monitor","preco":1500.0}],"status":"RECEBIDO","valorTotal":1800.0}
{"produtos":[{"nome":"Smartphone","preco":2500.0},{"nome":"Carregador","preco":100.0},{"nome":"Fone de Ouvido","preco":150.0}],"status":"RECEBIDO","valorTotal":2750.0}
{"produtos":[{"nome":"Impressora","preco":800.0},{"nome":"Papel A4","preco":50.0},{"nome":"Cartucho","preco":120.0}],"status":"RECEBIDO","valorTotal":970.0}
{"produtos":[{"nome":"Webcam","preco":350.0},{"nome":"Microfone","preco":250.0}],"status":"RECEBIDO","valorTotal":600.0}
{"produtos":[{"nome":"Tablet","preco":1200.0},{"nome":"Capa Protetora","preco":80.0}],"status":"RECEBIDO","valorTotal":1280.0}
{"produtos":[{"nome":"HD Externo","preco":400.0},{"nome":"Cabo USB","preco":30.0}],"status":"RECEBIDO","valorTotal":430.0}
{"produtos":[{"nome":"Roteador","preco":180.0},{"nome":"Cabo de Rede","preco":45.0}],"status":"RECEBIDO","valorTotal":225.0}
{"produtos":[{"nome":"Placa de Vídeo","preco":3500.0},{"nome":"Fonte de Alimentação","preco":450.0}],"status":"RECEBIDO","valorTotal":3950.0}
{"produtos":[{"nome":"SSD","preco":500.0},{"nome":"Memória RAM","preco":350.0}],"status":"RECEBIDO","valorTotal":850.0}
EOF
  echo "Mensagens enviadas para o tópico 'pedido-topic'."
}

# Execução
wait_for_kafka
create_topics
populate_topic
echo "Configuração do Kafka concluída."

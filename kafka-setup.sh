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

# Excluir tópicos existentes
function delete_topics() {
  for TOPIC in "${TOPICS[@]}"; do
    echo "Excluindo tópico: $TOPIC (se existir)..."
    kafka-topics --delete \
      --bootstrap-server $KAFKA_BROKER \
      --topic $TOPIC \
      || echo "Falha ao excluir $TOPIC ou o tópico não existe."
  done
  sleep 5  # Esperar alguns segundos para a exclusão ser propagada
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
{"itens":[{"quantidade":2,"produto":{"nome":"Arroz 5kg","preco":25.0}},{"quantidade":1,"produto":{"nome":"Feijão 1kg","preco":8.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":1,"produto":{"nome":"Óleo de Soja","preco":9.0}},{"quantidade":1,"produto":{"nome":"Macarrão","preco":5.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":2,"produto":{"nome":"Café 500g","preco":15.0}},{"quantidade":1,"produto":{"nome":"Açúcar 1kg","preco":6.0}},{"quantidade":1,"produto":{"nome":"Leite 1L","preco":7.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":1,"produto":{"nome":"Farinha de Trigo","preco":8.0}},{"quantidade":1,"produto":{"nome":"Sal 1kg","preco":3.0}},{"quantidade":1,"produto":{"nome":"Fermento","preco":5.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":2,"produto":{"nome":"Refrigerante 2L","preco":8.0}},{"quantidade":1,"produto":{"nome":"Suco 1L","preco":6.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":1,"produto":{"nome":"Cerveja Pack","preco":35.0}},{"quantidade":1,"produto":{"nome":"Água Mineral","preco":2.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":2,"produto":{"nome":"Molho de Tomate","preco":4.0}},{"quantidade":1,"produto":{"nome":"Extrato de Tomate","preco":3.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":1,"produto":{"nome":"Biscoito","preco":5.0}},{"quantidade":1,"produto":{"nome":"Bolacha","preco":4.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":2,"produto":{"nome":"Achocolatado","preco":12.0}},{"quantidade":1,"produto":{"nome":"Leite em Pó","preco":15.0}}],"status":"RECEBIDO","valorTotal":null}
{"itens":[{"quantidade":1,"produto":{"nome":"Óleo de Milho","preco":12.0}},{"quantidade":1,"produto":{"nome":"Vinagre","preco":4.0}}],"status":"RECEBIDO","valorTotal":null}
EOF
  echo "Mensagens enviadas para o tópico 'pedido-topic'."
}

# Execução
wait_for_kafka
delete_topics
create_topics
populate_topic
echo "Configuração do Kafka concluída."

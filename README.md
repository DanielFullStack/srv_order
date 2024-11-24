# Order Service - Gerenciamento de Pedidos

Este projeto é uma aplicação de gerenciamento de pedidos desenvolvida com **Spring Boot**. Ele utiliza **Kafka** para processamento assíncrono, **PostgreSQL** como banco de dados, e **Docker Compose** para gerenciar todos os serviços necessários. Além disso, a aplicação conta com monitoramento com **Spring Actuator**, e documentação interativa via **Swagger**.

---

## Pré-requisitos

Certifique-se de que os seguintes itens estão instalados na sua máquina:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)

---

## Tecnologias Utilizadas

- **Spring Boot** - Framework principal da aplicação.
- **PostgreSQL** - Banco de dados para armazenar pedidos.
- **Kafka** - Processamento assíncrono de mensagens.
- **Docker & Docker Compose** - Gerenciamento de contêineres.
- **Spring Actuator** - Monitoramento e métricas.
- **Swagger** - Documentação da API.

---

## Estrutura do Projeto

```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.backend.srv_order
│   │   │   │   ├── config                  # Configurações da aplicação
│   │   │   │   ├── controller              # Endpoints REST
│   │   │   │   ├── kafka                   # Producer e Consumer Kafka
│   │   │   │   ├── model                   # Entidades e modelos
│   │   │   │   ├── repository              # Interfaces de repositório
│   │   │   │   ├── service                 # Lógica de negócio
│   │   │   │   ├── Application.java        # Classe principal
│   │   │   └── resources
│   │   │       ├── application.properties  # Configurações
│   │   └── test                            # Testes unitários
├── Dockerfile                              # Dockerfile para construir a aplicação
├── docker-compose.yml                      # Configuração do ambiente com Kafka e PostgreSQL
├── pom.xml                                 # Configuração do Maven
└── README.md                               # Documentação
```

---

## Configuração e Execução do Projeto

### 1. Clonar o Repositório
```
https://github.com/DanielFullStack/srv_order.git
cd srv_order
```

### 2. Construir o Projeto
Certifique-se de que o Maven está instalado e compile o projeto para gerar o `.jar`:
```
mvn clean package
```

### 3. Executar com Docker Compose
Para iniciar a aplicação com todos os serviços necessários:
```
docker-compose up --build
```

### 4. Acessar os Serviços
- **Aplicação**: [http://localhost:8080](http://localhost:8080)
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **PostgreSQL**: `localhost:5432` (usuário: `postgres`, senha: `postgres`)
- **Kafka Broker**: `localhost:9092`
- **Kafka Topics UI**: [http://localhost:8081](http://localhost:8081)

---

## Configurações Personalizadas

### Variáveis de Ambiente no Docker Compose

As seguintes variáveis podem ser ajustadas no `docker-compose.yml`:

- **Banco de Dados**:
  - `POSTGRES_DB`: Nome do banco de dados (padrão: `orderdb`)
  - `POSTGRES_USER`: Usuário do banco (padrão: `postgres`)
  - `POSTGRES_PASSWORD`: Senha do banco (padrão: `postgres`)

- **Kafka**:
  - `KAFKA_BROKER_ID`: ID do broker Kafka (padrão: `1`)
  - `KAFKA_ZOOKEEPER_CONNECT`: Endereço do Zookeeper (padrão: `zookeeper:2181`)

---

## Monitoramento com Spring Actuator

O Spring Actuator está habilitado para fornecer métricas e informações sobre a aplicação:

- **Health Check**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- **Métricas**: [http://localhost:8080/actuator/metrics](http://localhost:8080/actuator/metrics)

---

## Documentação da API

A documentação interativa da API está disponível via Swagger:

- **URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Comandos Úteis

### Subir apenas a aplicação
```
docker-compose up --build -d
```

### Derrubar todos os contêineres
```
docker-compose down
```

### Escalar a aplicação
```
docker-compose up --scale srv_order=3
```
### Adicionar novos itens ao tópico Kafka
Para adicionar novos itens ao tópico, utilize o seguinte formato JSON no painel de administração do Kafka:
```
{"itens":[{"quantidade":2,"produto":{"nome":"Arroz 5kg","preco":25.0}},{"quantidade":1,"produto":{"nome":"Feijão 1kg","preco":8.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":1,"produto":{"nome":"Óleo de Soja","preco":9.0}},{"quantidade":1,"produto":{"nome":"Macarrão","preco":5.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":2,"produto":{"nome":"Café 500g","preco":15.0}},{"quantidade":1,"produto":{"nome":"Açúcar 1kg","preco":6.0}},{"quantidade":1,"produto":{"nome":"Leite 1L","preco":7.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":1,"produto":{"nome":"Farinha de Trigo","preco":8.0}},{"quantidade":1,"produto":{"nome":"Sal 1kg","preco":3.0}},{"quantidade":1,"produto":{"nome":"Fermento","preco":5.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":2,"produto":{"nome":"Refrigerante 2L","preco":8.0}},{"quantidade":1,"produto":{"nome":"Suco 1L","preco":6.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":1,"produto":{"nome":"Cerveja Pack","preco":35.0}},{"quantidade":1,"produto":{"nome":"Água Mineral","preco":2.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":2,"produto":{"nome":"Molho de Tomate","preco":4.0}},{"quantidade":1,"produto":{"nome":"Extrato de Tomate","preco":3.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":1,"produto":{"nome":"Biscoito","preco":5.0}},{"quantidade":1,"produto":{"nome":"Bolacha","preco":4.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":2,"produto":{"nome":"Achocolatado","preco":12.0}},{"quantidade":1,"produto":{"nome":"Leite em Pó","preco":15.0}}],"status":"RECEBIDO","valorTotal":null}
```
```
{"itens":[{"quantidade":1,"produto":{"nome":"Óleo de Milho","preco":12.0}},{"quantidade":1,"produto":{"nome":"Vinagre","preco":4.0}}],"status":"RECEBIDO","valorTotal":null}
```
---

## Pontos Extras
### 1. **Verificação de duplicação de pedidos**:
- Verificação de duplicação de pedidos antes de inserir no banco de dados.
  ```java
    private void validarDuplicidadePedido(Pedido pedido) {
      // Recupera todos os pedidos com o mesmo status do banco
      List<Pedido> pedidosExistentes = pedidoRepository.findByStatus(pedido.getStatus());
  
      // Formata a lista de itens do pedido atual para o padrão "nome-preco-quantidade"
      List<String> itensAtuais = pedido.getItens().stream()
              .map(item -> {
                  Produto produto = item.getProduto();
                  return produto.getNome() + "-" + produto.getPreco() + "-" + item.getQuantidade();
              })
              .toList();
  
      // Itera sobre os pedidos existentes para verificar duplicidade
      for (Pedido pedidoExistente : pedidosExistentes) {
          // Valida se o número de itens é igual
          if (pedidoExistente.getItens().size() != pedido.getItens().size()) {
              continue;
          }
  
          // Formata a lista de itens do pedido existente
          List<String> itensExistentes = pedidoExistente.getItens().stream()
                  .map(item -> {
                      Produto produto = item.getProduto();
                      return produto.getNome() + "-" + produto.getPreco() + "-" + item.getQuantidade();
                  })
                  .toList();
  
          // Verifica se todos os itens são iguais
          if (itensAtuais.containsAll(itensExistentes) && itensExistentes.containsAll(itensAtuais)) {
              logger.error("Pedido duplicado detectado com os seguintes itens: {}", itensAtuais);
              throw new RuntimeException("Pedido duplicado detectado. Estrutura: " + itensAtuais);
          }
      }
  
      logger.info("Nenhum pedido duplicado detectado.");
    }
  ```
- Envio de pedidos processados para novo tópico Kafka.
  ```java
  // Processa o pedido por ID e envia para outro tópico
    public Pedido processarPedido(Long id) {
        logger.info("Iniciando processamento do pedido ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado com ID: {}", id);
                    return new RuntimeException("Pedido não encontrado com ID: " + id);
                });
        double valorTotal = pedido.getItens()
                .stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
        pedido.setValorTotal(valorTotal);
        pedido.setStatus(PedidoStatusEnum.PROCESSADO.name());
        pedidoRepository.save(pedido);
        logger.info("Enviando pedido processado para o tópico Kafka");
        kafkaProducer.enviarPedido(KafkaPedidoTopicEnum.PEDIDO_PROCESSADO_TOPIC.getTopic(), pedido);
        return pedido;
    }
  ```
- Envio de pedidos cancelados para novo tópico.
  ```java
    // Cancela o pedido por ID e envia para outro tópico
    public Pedido cancelarPedido(Long id) {
        logger.info("Iniciando cancelamento do pedido ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pedido não encontrado com ID: {}", id);
                    return new RuntimeException("Pedido não encontrado com ID: " + id);
                });
        pedido.setStatus(PedidoStatusEnum.CANCELADO.name());
        pedidoRepository.save(pedido);
        logger.info("Enviando pedido cancelado para o tópico Kafka");
        kafkaProducer.enviarPedido(KafkaPedidoTopicEnum.PEDIDO_CANCELADO_TOPIC.getTopic(), pedido);
        return pedido;
    }
  ```

### 2. **Alta volumetria de pedidos**:
- Possibilidade de configuração do tópico kafka em várias partições.
  ```
    kafka-topics.sh --create \
    --topic pedido-topic \
    --bootstrap-server localhost:9092 \
    --partitions 10 \
    --replication-factor 3
  ```

- Configuração de grupos de consumidores para consumir pedidos de forma paralela.
  ```application.yml
    spring:
      kafka:
        consumer:
          group-id: pedido-consumer-group
          bootstrap-servers: localhost:9092
          auto-offset-reset: earliest
          enable-auto-commit: false
        listener:
          concurrency: 5 # Número de threads paralelas para consumir
  ```

  ```@KafkaListener
    @KafkaListener(topics = "pedido-topic", groupId = "pedido-consumer-group", concurrency = "5")
    public void consumePedidos(String message) {
        log.info("Mensagem recebida: {}", message);
        // Processa os pedidos
    }
  ```
### 3. **Consistência de dados e concorrência**:
- Utilização de transações para garantir consistência dos dados.
  ```application.yml
    spring:
      kafka:
        producer:
          transaction-id-prefix: pedido-tx-
        consumer:
          enable-auto-commit: false
  ```

  ```PedidoService
    @Service
    @Transactional // Garantia de consistência nos commits
    public class PedidoService {

        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;

        @Autowired
        private PedidoRepository pedidoRepository;

        public void processarPedido(Pedido pedido) {
            try {
                // Salvar pedido no banco
                pedidoRepository.save(pedido);

                // Enviar evento para o Kafka
                kafkaTemplate.executeInTransaction(operations -> {
                    operations.send("pedido-processado-topic", pedido.getId(), pedido.toString());
                    return true;
                });

                log.info("Pedido processado com sucesso: {}", pedido.getId());
            } catch (Exception e) {
                log.error("Erro ao processar pedido: {}", e.getMessage());
                throw e;
            }
        }
    }
  ```
- Configuração de lock otimista/pessimista em tabelas críticas, dependendo do fluxo de dados.
  - Lock otimista: Com controle de versão, útil para evitar conflitos em atualizações concorrentes.
    ```
      @Entity
      public class Pedido {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;

          @Version // Controla versão para evitar conflito de concorrência
          private Integer version;

          private String status;

          // Getters e setters
      }

    ```
  - Lock pessimista: Para garantir que apenas uma transação acesse o recurso por vez.
    ```
      @Query("SELECT p FROM Pedido p WHERE p.id = :id")
      @Lock(LockModeType.PESSIMISTIC_WRITE) // Bloqueia para escrita
      Optional<Pedido> findByIdWithLock(@Param("id") Long id);
    ```
### 4. **adequação do banco de dados**:
- Utilização de pgAdmin para identificar gargalos no banco.
  - Você pode usar o pgAdmin ou ferramentas CLI como EXPLAIN e ANALYZE para otimizar as queries. 
    Por exemplo:
    ```
      EXPLAIN ANALYZE
      SELECT * FROM pedido WHERE status = 'PROCESSADO';
    ```
- Possibilidade de particionamento de tabelas ou sharding.
  - Se a tabela pedido se tornar muito grande, implemente particionamento no PostgreSQL com base em colunas como status ou data.
    Exemplo: Particionamento por range de datas
    ```
    CREATE TABLE pedido (
        id BIGSERIAL PRIMARY KEY,
        data_criacao DATE NOT NULL,
        status VARCHAR(20)
    ) PARTITION BY RANGE (data_criacao);

    CREATE TABLE pedido_2023 PARTITION OF pedido
    FOR VALUES FROM ('2023-01-01') TO ('2024-01-01');
    ```
  - Se o particionamento não for suficiente, avalie sharding usando ferramentas como Citus (extensão para PostgreSQL).
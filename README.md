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


---

## Configuração e Execução do Projeto

### 1. Clonar o Repositório

https://github.com/DanielFullStack/srv_order.git
cd srv_order


### 2. Construir o Projeto
Certifique-se de que o Maven está instalado e compile o projeto para gerar o `.jar`:

mvn clean package


### 3. Executar com Docker Compose
Para iniciar a aplicação com todos os serviços necessários:

docker-compose up --build


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

### Exemplos de Endpoints

#### Criar Pedido
- **POST**: `/api/pedidos`
- **Body**:

#### Exemplo 1: Pedido com dois produtos
```json
{
  "status": "RECEBIDO",
  "produtos": [
    {
      "nome": "Produto A",
      "preco": 19.99
    },
    {
      "nome": "Produto B",
      "preco": 35.50
    }
  ]
}
```

#### Exemplo 2: Pedido com um único produto
```json
{
  "status": "PROCESSADO",
  "produtos": [
    {
      "nome": "Produto C",
      "preco": 120.00
    }
  ],
  "valorTotal": 120.00
}
```

#### Exemplo 3: Pedido vazio (sem produtos)
```json
{
  "status": "RECEBIDO",
  "produtos": [],
  "valorTotal": 0.00
}
```

#### Exemplo 4: Pedido com múltiplos produtos e valor total calculado
```json
{
  "status": "PROCESSADO",
  "produtos": [
    {
      "nome": "Produto D",
      "preco": 15.75
    },
    {
      "nome": "Produto E",
      "preco": 45.30
    },
    {
      "nome": "Produto F",
      "preco": 29.99
    }
  ],
  "valorTotal": 91.04
}
```

#### Exemplo 5: Pedido com produtos com preços variados
```json
{
  "status": "RECEBIDO",
  "produtos": [
    {
      "nome": "Produto G",
      "preco": 10.00
    },
    {
      "nome": "Produto H",
      "preco": 99.99
    },
    {
      "nome": "Produto I",
      "preco": 5.49
    }
  ],
  "valorTotal": 115.48
}
```
  

#### Processar Pedido
- **POST**: `/api/pedidos/processar`

#### Exemplo 1: Pedido válido com dois produtos
```json
{
  "id": 1,
  "status": "RECEBIDO",
  "produtos": [
    {
      "id": 101,
      "nome": "Produto X",
      "preco": 25.00
    },
    {
      "id": 102,
      "nome": "Produto Y",
      "preco": 50.00
    }
  ]
}
```

#### Exemplo 2: Pedido válido com um único produto
```json
{
  "id": 2,
  "status": "RECEBIDO",
  "produtos": [
    {
      "id": 103,
      "nome": "Produto Z",
      "preco": 75.50
    }
  ]
}
```

#### Exemplo 3: Pedido com produtos sem preço (inválido)
```json
{
  "id": 3,
  "status": "RECEBIDO",
  "produtos": [
    {
      "id": 104,
      "nome": "Produto Sem Preço",
      "preco": null
    }
  ]
}
```

#### Exemplo 4: Pedido com status inválido
```json
{
  "id": 4,
  "status": "INVALIDO",
  "produtos": [
    {
      "id": 105,
      "nome": "Produto A",
      "preco": 30.00
    }
  ]
}
```

#### Exemplo 5: Pedido vazio (sem produtos)
```json
{
  "id": 5,
  "status": "RECEBIDO",
  "produtos": []
}
```

#### Exemplo 6: Pedido com valores extremos nos preços
```json
{
  "id": 6,
  "status": "RECEBIDO",
  "produtos": [
    {
      "id": 106,
      "nome": "Produto Barato",
      "preco": 0.01
    },
    {
      "id": 107,
      "nome": "Produto Caro",
      "preco": 99999.99
    }
  ]
}
```

#### Exemplo 7: Pedido com produtos duplicados
```json
{
  "id": 7,
  "status": "RECEBIDO",
  "produtos": [
    {
      "id": 108,
      "nome": "Produto Repetido",
      "preco": 10.00
    },
    {
      "id": 108,
      "nome": "Produto Repetido",
      "preco": 10.00
    }
  ]
}
```
---

## Desenvolvimento e Debug

### Construção Local

Para executar a aplicação sem Docker:

1. Certifique-se de que o PostgreSQL e o Kafka estão em execução localmente.
2. Configure as variáveis de ambiente no `application.properties` ou via CLI:
   
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/orderdb
   SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
   
3. Inicie a aplicação:
   
   mvn spring-boot:run
   

### Testes Unitários

Execute os testes unitários com o comando:

mvn test


---

## Comandos Úteis

### Subir apenas a aplicação

docker-compose up --build -d


### Derrubar todos os contêineres

docker-compose down


### Escalar a aplicação

docker-compose up --scale srv_order=3

---
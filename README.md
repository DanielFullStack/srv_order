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
- **POST**: `/api/orders`
- **Body**:
```  
  {
    "produto": "Notebook",
    "quantidade": 2
  }
```  

#### Consultar Pedido
- **GET**: `/api/orders/{id}`

---

## Desenvolvimento e Debug

### Construção Local

Para executar a aplicação sem Docker:

1. Certifique-se de que o PostgreSQL e o Kafka estão em execução localmente.
2. Configure as variáveis de ambiente no `application.properties` ou via CLI:
```   
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/orderdb
   SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```   
3. Inicie a aplicação:
```   
   mvn spring-boot:run
```   

### Testes Unitários

Execute os testes unitários com o comando:
```
mvn test
```

---

## Comandos Úteis

### Subir apenas a aplicação
```
docker-compose up --build app
```

### Derrubar todos os contêineres
```
docker-compose down
```

### Escalar a aplicação
```
docker-compose up --scale app=3
```

---
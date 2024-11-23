FROM maven:3.8.4-openjdk-17-slim AS build

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar os arquivos do projeto
COPY pom.xml .
COPY src ./src

RUN chmod +x kafka-setup.sh
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar o arquivo JAR da aplicação do estágio de build
COPY --from=build /app/target/srv_order-0.0.1-SNAPSHOT.jar srv_order.jar

# Expor a porta em que a aplicação será executada
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "srv_order.jar"]
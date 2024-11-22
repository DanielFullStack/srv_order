# Usar uma imagem base do OpenJDK para o ambiente de execução
FROM openjdk:17-jdk-slim

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR da aplicação para o contêiner
COPY target/order-service.jar order-service.jar

# Expor a porta em que a aplicação será executada
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "order-service.jar"]

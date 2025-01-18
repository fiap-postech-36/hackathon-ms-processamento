# Etapa 1: Build
FROM maven:3.9.8-amazoncorretto-17 AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo pom.xml e os arquivos pom dos módulos para o contêiner
COPY pom.xml .

COPY application/pom.xml application/
COPY domain/pom.xml domain/
COPY infra/pom.xml infra/

# Faz o download das dependências
RUN mvn dependency:go-offline

# Copia todo o código fonte do projeto para o contêiner
COPY . .

# Compila o projeto e empacota o aplicativo como um arquivo JAR
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM amazoncorretto:17-alpine

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o JAR do estágio de build para o estágio de runtime
COPY --from=builder /app/application/target/*.jar app.jar

# Exponhe a porta em que a aplicação irá rodar
EXPOSE 8080

# Define o comando para iniciar a aplicação
CMD ["java", "-jar", "app.jar"]

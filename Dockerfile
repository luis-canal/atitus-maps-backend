# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiar pom.xml
COPY pom.xml .

# Copiar código fonte
COPY src src

# Build da aplicação
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR do estágio anterior
COPY --from=builder /app/target/api-sample-*.jar app.jar

# Expor porta
EXPOSE 8080

# Variável de ambiente para JVM
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"

# Comando para iniciar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]

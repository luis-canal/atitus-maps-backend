# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar arquivo Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copiar código fonte
COPY src src

# Build da aplicação
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

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

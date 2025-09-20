# ---- Build stage ----
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

# Copy Maven wrapper and project files from root
COPY ../mvnw ../pom.xml ../.mvn/ ./

# Ensure wrapper is executable
RUN chmod +x ./mvnw

# Pre-download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy actual app source
COPY . .

# Build the JAR
RUN ./mvnw clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR from builder
COPY --from=builder /workspace/target/fitness-aggregator-api.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
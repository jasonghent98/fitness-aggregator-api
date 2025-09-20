# ---- Build stage ----
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace

# Copy Maven wrapper + root pom.xml
COPY mvnw pom.xml .mvn/ ./

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy module sources
COPY fitness-aggregator-api ./fitness-aggregator-api

WORKDIR /workspace/fitness-aggregator-api
RUN ../mvnw clean package -DskipTests
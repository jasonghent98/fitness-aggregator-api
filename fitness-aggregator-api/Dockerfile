# Use a lightweight JRE base image for Java 21
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built jar file into the container
COPY target/fitness-aggregator-api.jar app.jar

# Cloud Run sends traffic to this env var ($PORT)
ENV PORT=8080

# Expose the port (optional, Cloud Run sets it automatically)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
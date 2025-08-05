# Use official OpenJDK image with JDK 17
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file (replace this with your actual JAR name if known)
COPY target/fitness-aggregator-api.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
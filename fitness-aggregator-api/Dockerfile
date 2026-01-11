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

# Run the application with JVM memory tuning
# Cloud Run default is 512Mi, we use 80% for heap (384m)
# Remaining 128m for native memory (threads, direct buffers, etc)
ENTRYPOINT ["java", \
    "-Xms384m", \
    "-Xmx384m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=100", \
    "-XX:+UseStringDeduplication", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
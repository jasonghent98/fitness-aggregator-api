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

# Run the application with aggressive memory tuning for 512Mi container
# Heap: 300m, Metaspace: 80m, leaving ~130m for threads/native
ENTRYPOINT ["java", \
    "-Xms256m", \
    "-Xmx300m", \
    "-XX:MaxMetaspaceSize=80m", \
    "-XX:ReservedCodeCacheSize=64m", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-XX:+UnlockExperimentalVMOptions", \
    "-XX:+UseContainerSupport", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.main.lazy-initialization=true", \
    "-jar", "app.jar"]
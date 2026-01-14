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

# Run the application with memory tuning for 512Mi container
# Heap: 220m, Metaspace: 128m, CodeCache: 48m = ~396m managed
# Leaves ~116m for threads, native buffers, etc.
ENTRYPOINT ["java", \
    "-Xms180m", \
    "-Xmx220m", \
    "-XX:MaxMetaspaceSize=128m", \
    "-XX:ReservedCodeCacheSize=48m", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-XX:+UnlockExperimentalVMOptions", \
    "-XX:+UseContainerSupport", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.main.lazy-initialization=true", \
    "-jar", "app.jar"]
# Java Backend Memory Health Check Report
**Date:** 2026-01-11
**Status:** ⚠️ CRITICAL - Multiple memory issues identified

## Issues Identified

### 🔴 CRITICAL: No JVM Memory Configuration
**Location:** `Dockerfile`
**Impact:** HIGH - Java will use default heap sizes, likely causing OOM errors

**Current:**
```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Problem:** No `-Xmx` (max heap) or `-Xms` (initial heap) specified. Cloud Run containers have memory limits, but Java doesn't know about them.

---

### 🔴 CRITICAL: No Cloud Run Memory Limits
**Location:** Deployment scripts
**Impact:** HIGH - Container can use all available memory

**Problem:** `scripts/prod/deploy.sh` doesn't specify `--memory` flag for Cloud Run.

---

### 🟡 HIGH: No RestTemplate Connection Pooling
**Location:** `config/RestTemplateConfig.java:15`
**Impact:** MEDIUM - HTTP connections may not be reused properly

**Current:**
```java
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

**Problem:** Using default RestTemplate without connection pooling can cause connection exhaustion and memory leaks.

---

### 🟡 HIGH: No Hibernate/HikariCP Connection Pool Configuration
**Location:** `application.yml`
**Impact:** MEDIUM - Database connections not optimally managed

**Problem:** No explicit configuration for:
- `spring.datasource.hikari.maximum-pool-size`
- `spring.datasource.hikari.minimum-idle`
- `spring.jpa.properties.hibernate.jdbc.batch_size`

---

### 🟡 MEDIUM: Large Data Fetches Without Pagination
**Location:** `service/garmin/GarminService.java:49-100`
**Impact:** MEDIUM - Loading all data into memory at once

**Problem:** Methods like `getActivityForUserForGivenRange()` return entire `List<>` without pagination. For users with years of data (365 days for ELITE tier), this loads thousands of records.

---

### 🟢 LOW: No SQL Query Result Size Limits
**Location:** Repository queries
**Impact:** LOW - But could grow over time

**Problem:** No `@QueryHints` or result size limits on JPA queries.

---

## Recommended Fixes

### 1. Update Dockerfile with JVM Memory Settings

```dockerfile
# Use a lightweight JRE base image for Java 21
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY target/fitness-aggregator-api.jar app.jar

ENV PORT=8080
EXPOSE 8080

# JVM Memory Configuration
# Cloud Run default is 512Mi, we'll use 80% for heap
ENTRYPOINT ["java", \
    "-Xms384m", \
    "-Xmx384m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=100", \
    "-XX:+UseStringDeduplication", \
    "-jar", "app.jar"]
```

### 2. Update Cloud Run Deployment

```bash
# In scripts/prod/deploy.sh, add:
gcloud run deploy "${SERVICE}" \
  --project "${PROJECT}" \
  --region "${REGION}" \
  --image "${IMAGE}" \
  --service-account "${RUNTIME_SA}" \
  --add-cloudsql-instances "${CONN}" \
  --env-vars-file "${ENV_FILE}" \
  --platform managed \
  --memory 512Mi \              # ADD THIS
  --cpu 1 \                     # ADD THIS
  --timeout 60s \               # ADD THIS
  --max-instances 10            # ADD THIS (prevent runaway costs)
```

### 3. Configure RestTemplate with Connection Pooling

```java
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory =
            new HttpComponentsClientHttpRequestFactory();

        HttpClient httpClient = HttpClients.custom()
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(20)
                .setConnectionTimeToLive(30, TimeUnit.SECONDS)
                .build();

        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);

        return new RestTemplate(factory);
    }
}
```

**Required dependency in pom.xml:**
```xml
<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
</dependency>
```

### 4. Add HikariCP and Hibernate Configuration

```yaml
# In application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 5        # Cloud Run typically has 1-2 CPU
      minimum-idle: 2
      connection-timeout: 10000
      idle-timeout: 300000
      max-lifetime: 600000
      leak-detection-threshold: 60000
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
          fetch_size: 50
        order_inserts: true
        order_updates: true
        generate_statistics: false
```

### 5. Add Pagination Support (Future Enhancement)

Consider adding pagination to large data fetches:

```java
// Example for future implementation
public Page<GarminActivitySummary> getActivityForUserForGivenRange(
        UUID userId,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
) {
    return garminActivityRepo.findByActualizeUserIdAndCalendarDateBetween(
        userId, startDate, endDate, pageable
    );
}
```

---

## Immediate Action Items

1. ✅ **Update Dockerfile** with JVM memory settings
2. ✅ **Update deployment scripts** with memory limits
3. ✅ **Add RestTemplate connection pooling**
4. ✅ **Add HikariCP configuration** to application.yml
5. ⏸️ **Monitor** memory usage after deployment
6. ⏸️ **Consider pagination** if datasets continue to grow

---

## Monitoring Recommendations

After deploying fixes, monitor:
- **Heap usage:** Should stay under 80% of Xmx
- **GC frequency:** G1GC should keep pauses under 100ms
- **Connection pool stats:** HikariCP metrics via actuator
- **Cloud Run metrics:** Memory utilization, request latency

---

## Estimated Impact

| Issue | Current Risk | After Fix |
|-------|-------------|-----------|
| OOM Errors | HIGH | LOW |
| Connection Leaks | MEDIUM | LOW |
| Query Performance | MEDIUM | MEDIUM |
| Scalability | LOW | MEDIUM |

---

## Additional Notes

- The current error shows `java.lang.OutOfMemoryError` in the NIO Poller thread, suggesting the heap is completely exhausted
- Without JVM limits, Java may try to use more memory than Cloud Run allows, causing container kills
- The G1GC collector is recommended for Cloud Run's low-latency requirements

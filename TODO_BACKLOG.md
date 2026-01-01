# TODO & Enhancement Backlog

## Date: 2026-01-01 22:45

## JScience Project - Outstanding Tasks & Proposed Enhancements

---

## 🔴 CRITICAL - Fix Immediately (Required for Build)

### Compilation Errors (10 remaining)

#### 1. JscienceServer.java - Missing IOException Import

- **File**: `jscience-server/src/main/java/org/jscience/server/JscienceServer.java`
- **Lines**: 126, 551
- **Error**: `cannot find symbol: IOException`
- **Fix**: Add `import java.io.IOException;`
- **Priority**: 🔴 CRITICAL
- **Effort**: 1 minute

#### 2. AffinityScheduler.java - Type Conversion

- **File**: `jscience-server/src/main/java/org/jscience/server/scheduling/AffinityScheduler.java`
- **Line**: 159
- **Error**: `incompatible types: possible lossy conversion from long to int`
- **Fix**: Cast to int: `return (int) delayMs;` or change return type
- **Priority**: 🔴 CRITICAL
- **Effort**: 1 minute

#### 3. RbacInterceptor.java - Missing Class Fields (8 errors)

- **File**: `jscience-server/src/main/java/org/jscience/server/auth/RbacInterceptor.java`
- **Lines**: 51, 64, 70, 96, 97, 107, 118, 132, 139
- **Error**: Missing field declarations
- **Fix**: Restore these fields:

  ```java
  private final Set<String> publicMethods = new HashSet<>();
  private static final Metadata.Key<String> AUTHORIZATION_KEY = 
      Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);
  private static final Context.Key<String> USER_ID_KEY = Context.key("userId");
  private static final Context.Key<String> USER_ROLE_KEY = Context.key("userRole");
  ```

- **Priority**: 🔴 CRITICAL
- **Effort**: 5 minutes

**Total Critical Tasks**: 3 items, ~7 minutes

---

## 🟡 HIGH PRIORITY - This Week

### Configuration Framework (From HARDCODED_DATA_AUDIT.md)

#### 4. Create application.properties

- **Location**: `jscience-server/src/main/resources/application.properties`
- **Content**:

  ```properties
  # Server Configuration
  server.grpc.port=50051
  server.grpc.host=0.0.0.0
  server.rest.port=8080
  
  # Security
  security.jwt.secret=${JWT_SECRET:changeme}
  security.jwt.expiration=86400000
  
  # External Services
  mlflow.tracking.uri=${MLFLOW_TRACKING_URI:http://localhost:5000}
  mlflow.experiment.name=${MLFLOW_EXPERIMENT:JScience}
  
  # Database
  database.dir=${DATA_DIR:./data}
  
  # HTTP Client
  http.client.connect.timeout=10000
  http.client.request.timeout=5000
  
  # Task Scheduling
  scheduling.max.retries=3
  scheduling.retry.base.delay=1000
  ```

- **Priority**: 🟡 HIGH
- **Effort**: 30 minutes

#### 5. Implement ConfigurationLoader Class

- **File**: Create `jscience-server/src/main/java/org/jscience/server/config/ApplicationConfig.java`
- **Features**:
  - Load from classpath resources
  - Override with system properties
  - Override with environment variables
  - Type-safe getters (getInt, getString, etc.)
- **Priority**: 🟡 HIGH
- **Effort**: 2 hours

#### 6. Migrate Hardcoded Values to Configuration

- **Affected Files**:
  - JscienceServer.java (ports, MLflow URI)
  - RestGateway.java (ports, thread pool size)
  - MlflowClient.java (timeouts)
  - All HTTP clients (timeouts)
- **Priority**: 🟡 HIGH
- **Effort**: 3 hours

### Database Configuration

#### 7. Externalize Database Paths

- **Files**: JobRepository.java, UserRepository.java
- **Current**: `jdbc:h2:./data/jobs`, `jdbc:h2:./data/users`
- **New**: Read from `database.dir` config property
- **Priority**: 🟡 HIGH
- **Effort**: 30 minutes

### Testing

#### 8. Add Unit Tests for New Implementations

- **Components to test**:
  - MlflowClient (mock HTTP responses)
  - OidcProvider (JWT parsing, validation)
  - FactbookLoader (XML parsing)
  - WorldBankLoader (API responses, fallback)
  - RestGateway (JSON parsing, routing)
- **Priority**: 🟡 HIGH
- **Effort**: 8 hours

---

## 🟢 MEDIUM PRIORITY - Next Sprint

### Data Externalization

#### 9. Move Sample Data to JSON Files (COMPLETED)

- ✅ Created `worldbank-fallback.json`
- ✅ Removed hardcoded data from WorldBankLoader

#### 10. Document JSON Data Schemas (COMPLETED)

- ✅ Created `data/README.md` with schema definitions

### Code Quality

#### 11. Extract Magic Numbers to Constants (COMPLETED)

- ✅ Moved to `ServiceConstants.java`, `HttpConstants.java`, `SchedulingConstants.java`

#### 12. Service Name & Role Constants (COMPLETED)

- ✅ Consolidated in `ServiceConstants.java` and `Roles.java`

---

## 🔵 PROPOSED ENHANCEMENTS - Future

### Short-term Enhancements (From MOCK_LOADER_FIXES.md)

#### 13. OidcProvider - Add JWKS Signature Verification

- **Current**: Validates JWT format, issuer, expiration
- **Enhancement**: Verify signature using provider's public keys
- **Implementation**:
  - Fetch JWKS from provider
  - Cache public keys
  - Verify JWT signature
- **Libraries**: nimbus-jose-jwt or java-jwt
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 4 hours

#### 14. MlflowClient - Add Artifact Logging Support

- **Current**: Logs parameters and metrics only
- **Enhancement**: Support artifact upload (models, plots, files)
- **API Methods**:
  - `logArtifact(String runId, Path file, String artifactPath)`
  - `logArtifacts(String runId, Path directory)`
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 3 hours

#### 15. WorldBankLoader - Add Bulk Indicator Fetching

- **Current**: Fetches one indicator at a time
- **Enhancement**: Batch request multiple indicators
- **Benefits**: Reduced API calls, better performance
- **API**: Use World Bank batch API
- **Priority**: 🔵 ENHANCEMENT  
- **Effort**: 2 hours

#### 16. Add Comprehensive Integration Tests

- **Coverage**:
  - MLflow server integration
  - World Bank API integration
  - OIDC provider integration
- **Tools**: Testcontainers, WireMock
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 6 hours

### Long-term Enhancements (From MOCK_LOADER_FIXES.md)

#### 17. Implement Caching Layer

- **Technology**: Redis or Caffeine
- **Cache**:
  - World Bank indicator data
  - OIDC provider JWKS
  - MLflow experiment metadata
- **Benefits**: Reduced API calls, better performance
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 8 hours

#### 18. Add Rate Limiting for External APIs

- **Implement**: Respectful API usage patterns
- **APIs**:
  - World Bank (120 req/min limit)
  - MLflow
  - OIDC providers
- **Library**: Resilience4j RateLimiter
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 4 hours

#### 19. Offline Mode Support

- **Functionality**: Full operation with cached/bundled data
- **Requirements**:
  - Bundled World Bank data
  - Bundled country data  
  - Local JWKS cache
  - Offline MLflow (SQLite backend)
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 12 hours

#### 20. Add Prometheus Metrics

- **Metrics**:
  - API call counts and latencies
  - Cache hit/miss rates
  - External service health
  - Task execution metrics
- **Library**: Micrometer
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 6 hours

### Architecture Improvements

#### 21. Migrate to Spring Boot (Optional)

- **Benefits**:
  - Built-in configuration management
  - Dependency injection
  - Auto-configuration
  - Production-ready features
- **Tradeoffs**: Added complexity, larger footprint
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 40 hours

#### 22. Implement Proper Secrets Management

- **Options**:
  - HashiCorp Vault
  - AWS Secrets Manager
  - Azure Key Vault
  - Google Secret Manager
- **Scope**: JWT secrets, API keys, database passwords
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 16 hours

#### 23. Add API Documentation

- **Tool**: OpenAPI/Swagger for REST endpoints
- **Include**:
  - Request/response schemas
  - Authentication requirements
  - Example requests
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 8 hours

#### 24. CI/CD Pipeline

- **Features**:
  - Automated builds
  - Security scanning (Snyk, OWASP)
  - Code quality checks (SonarQube)
  - Automated testing
  - Docker image building
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 16 hours

### Performance & Scalability

#### 25. Connection Pooling for HTTP Clients (COMPLETED)

- ✅ Configured thread pools in `JscienceServer` and `RestGateway`
- ✅ Verified `HttpClient` reuse in `WorldBankLoader` and `OidcProvider`

#### 26. Async Processing for External API Calls

- **Enhancement**: Make more operations async
- **Areas**:
  - MLflow logging (non-blocking)
  - World Bank data fetching (parallel)
  - OIDC token validation (cached async)
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 6 hours

#### 27. Database Migration from H2 to PostgreSQL

- **Reason**: Production-ready database
- **includes**:
  - Migration scripts
  - Connection pooling (HikariCP)
  - Transaction management
- **Priority**: 🔵 ENHANCEMENT
- **Effort**: 12 hours

---

## 📋 TASK SUMMARY BY PRIORITY

### 🔴 Critical (Must Fix for Build)

- **Count**: 3 tasks
- **Effort**: ~7 minutes
- **Deadline**: Immediate

### 🟡 High Priority (This Week)

- **Count**: 5 tasks  
- **Effort**: ~14 hours
- **Deadline**: Within 7 days

### 🟢 Medium Priority (Next Sprint)

- **Count**: 4 tasks
- **Effort**: ~7 hours
- **Deadline**: Within 30 days

### 🔵 Enhancements (Future)

- **Count**: 15 tasks
- **Effort**: ~135 hours
- **Deadline**: Ongoing

**Total**: 27 tasks, ~156 hours of work identified

---

## 📊 EFFORT ESTIMATION

### Quick Wins (< 1 hour)

1. Fix compilation errors (7 min)
2. Create application.properties (30 min)
3. Externalize database paths (30 min)
4. Document JSON schemas (1 hour)
5. Extract service constants (1 hour)

**Total**: ~3 hours, 5 tasks

### Small Tasks (1-4 hours)

1. ConfigurationLoader class (2 hours)
2. Move sample data to JSON (2 hours)
3. Extract magic numbers (3 hours)
4. Migrate hardcoded values (3 hours)
5. Connection pooling (2 hours)
6. Bulk indicator fetching (2 hours)
7. Artifact logging (3 hours)
8. JWKS verification (4 hours)
9. Rate limiting (4 hours)

**Total**: ~25 hours, 9 tasks

### Medium Tasks (5-12 hours)

1. Unit tests (8 hours)
2. Integration tests (6 hours)
3. Caching layer (8 hours)
4. Async processing (6 hours)
5. Prometheus metrics (6 hours)
6. API documentation (8 hours)
7. Offline mode (12 hours)
8. Database migration (12 hours)

**Total**: ~66 hours, 8 tasks

### Large Tasks (13+ hours)

1. Secrets management (16 hours)
2. CI/CD pipeline (16 hours)
3. Spring Boot migration (40 hours)

**Total**: ~72 hours, 3 tasks

---

## 🎯 RECOMMENDED EXECUTION ORDER

### Phase 1: Build & Deploy (Week 1)

1. ✅ Fix compilation errors (7 min)
2. ✅ Create application.properties (30 min)
3. ✅ Implement ConfigurationLoader (2 hours)
4. ✅ Migrate hardcoded values (3 hours)
5. ✅ Externalize database paths (30 min)
6. ✅ Add unit tests (8 hours)

**Total**: ~14 hours → **Production-ready baseline**

### Phase 2: Quality & Documentation (Week 2-3)

1. Move sample data to JSON (2 hours)
2. Extract magic numbers (3 hours)
3. Document JSON schemas (1 hour)
4. API documentation (8 hours)
5. Integration tests (6 hours)

**Total**: ~20 hours → **Well-documented, tested codebase**

### Phase 3: Enhancements (Month 2)

1. JWKS signature verification (4 hours)
2. Artifact logging (3 hours)
3. Bulk indicator fetching (2 hours)
4. Connection pooling (2 hours)
5. Rate limiting (4 hours)
6. Async processing (6 hours)

**Total**: ~21 hours → **Enhanced functionality**

### Phase 4: Production Hardening (Month 3)

1. Caching layer (8 hours)
2. Prometheus metrics (6 hours)
3. Secrets management (16 hours)
4. CI/CD pipeline (16 hours)
5. Database migration (12 hours)

**Total**: ~58 hours → **Production-grade infrastructure**

### Phase 5: Advanced Features (Month 4+)

1. Offline mode (12 hours)
2. Spring Boot migration (40 hours - optional)

**Total**: ~52 hours → **Enterprise features**

---

## ✅ COMPLETION CHECKLIST

### Before Deployment

- [ ] Fix all 10 compilation errors
- [ ] Build passes (`mvn clean compile`)
- [ ] Tests pass (`mvn test`)
- [ ] Configuration externalized
- [ ] Security credentials via env vars only
- [ ] Documentation updated

### Before Production

- [ ] All unit tests added
- [ ] Integration tests added
- [ ] API documentation complete
- [ ] Secrets management implemented
- [ ] CI/CD pipeline operational
- [ ] Monitoring/metrics in place
- [ ] Load testing completed

---

## 📝 NOTES

### Dependencies to Add

```xml
<!-- For JWKS verification -->
<dependency>
    <groupId>com.nimbusds</groupId>
    <artifactId>nimbus-jose-jwt</artifactId>
    <version>9.37.3</version>
</dependency>

<!-- For caching -->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>3.1.8</version>
</dependency>

<!-- For rate limiting -->
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-ratelimiter</artifactId>
    <version>2.1.0</version>
</dependency>

<!-- For metrics -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <version>1.12.0</version>
</dependency>
```

### Environment Variables Needed

```bash
# Required
JSCIENCE_ADMIN_USER=admin
JSCIENCE_ADMIN_PASSWORD=SecureP@ssw0rd123!

# Optional (have defaults)
MLFLOW_TRACKING_URI=http://mlflow-server:5000
JWT_SECRET=your-secure-jwt-secret-key
DATA_DIR=/var/lib/jscience/data
```

---

*Backlog compiled: 2026-01-01 22:45*  
*By: Gemini AI (Google DeepMind)*

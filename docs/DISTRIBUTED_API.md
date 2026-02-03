# JScience API Reference

## gRPC Services

### ComputeService

#### SubmitTask

Submit a computational task to the grid.

```protobuf
rpc SubmitTask(TaskRequest) returns (TaskResponse);

message TaskRequest {
    bytes serialized_task = 1;
    Priority priority = 2;
}

message TaskResponse {
    string task_id = 1;
    Status status = 2;
}
```

**Example:**

```java
TaskRequest request = TaskRequest.newBuilder()
    .setSerializedTask(ByteString.copyFrom(taskBytes))
    .setPriority(Priority.NORMAL)
    .build();
TaskResponse response = computeStub.submitTask(request);
```

#### GetResult

Retrieve the result of a completed task.

```protobuf
rpc GetResult(ResultRequest) returns (TaskResult);
```

#### GetStatus

Get current server status.

```protobuf
rpc GetStatus(Empty) returns (ServerStatus);

message ServerStatus {
    int32 active_workers = 1;
    int32 queued_tasks = 2;
    double system_load = 3;
}
```

---

### AuthService

#### Login

Authenticate and receive JWT token.

```protobuf
rpc Login(LoginRequest) returns (AuthResponse);

message LoginRequest {
    string username = 1;
    string password = 2;
}

message AuthResponse {
    bool success = 1;
    string token = 2;
    string message = 3;
}
```

#### Register

Create new user account.

```protobuf
rpc Register(RegisterRequest) returns (AuthResponse);
```

---

### CollaborationService

#### CreateSession

Create a new collaboration session.

```protobuf
rpc CreateSession(CreateSessionRequest) returns (SessionResponse);
```

#### JoinSession

Join an existing session (streaming).

```protobuf
rpc JoinSession(SessionRequest) returns (stream SessionEvent);
```

---

## REST API

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/status` | Get server status |
| GET | `/api/tasks` | List tasks |
| POST | `/api/tasks` | Submit task |
| GET | `/api/tasks/{id}` | Get task details |
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/register` | Register |
| GET | `/health` | Health check |
| GET | `/metrics` | Prometheus metrics |

### Examples

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"secret"}'

# Submit task
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"type":"compute","priority":"HIGH"}'

# Get status
curl http://localhost:8080/api/status
```

---

## Java SDK

### Task Submission

```java
// Create context
DistributedContext ctx = new GrpcDistributedContext("localhost", 50051);

// Submit task
Future<Double> result = ctx.submit(() -> {
    return computePi(1_000_000);
}, Priority.HIGH);

// Get result
Double pi = result.get(30, TimeUnit.SECONDS);
```

### Workflow Engine

```java
WorkflowEngine engine = new WorkflowEngine();

Workflow pipeline = WorkflowEngine.workflow("ml-train", "ML Training")
    .addStep(step("preprocess", "Preprocess", ctx -> loadData()))
    .addStep(step("train", "Train", ctx -> trainModel(ctx)).retries(3))
    .addStep(step("evaluate", "Evaluate", ctx -> evaluate(ctx)))
    .addDependency("train", "preprocess")
    .addDependency("evaluate", "train");

engine.registerWorkflow(pipeline);
CompletableFuture<ExecutionContext> result = engine.execute("ml-train", inputs);
```

### Circuit Breaker

```java
CircuitBreaker cb = new CircuitBreaker("external-api", 5, 3, Duration.ofSeconds(30));

try {
    String response = cb.execute(() -> callExternalAPI());
} catch (CircuitBreakerOpenException e) {
    // Circuit is open, use fallback
    return cachedValue;
}
```

### Rate Limiter

```java
RateLimiterInterceptor limiter = new RateLimiterInterceptor(100, 200);
limiter.configureMethod("ComputeService/SubmitTask", 50, 100);

ServerBuilder.forPort(50051)
    .intercept(limiter)
    .addService(computeService)
    .build();
```

---

## Metrics Reference

### Task Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `jscience_tasks_total` | Counter | Total submitted tasks |
| `jscience_tasks_completed` | Counter | Completed tasks |
| `jscience_tasks_failed` | Counter | Failed tasks |
| `jscience_task_duration_seconds` | Histogram | Task execution time |

### Worker Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `jscience_workers_active` | Gauge | Active workers |
| `jscience_workers_idle` | Gauge | Idle workers |
| `jscience_worker_cpu_usage` | Gauge | Worker CPU % |

### gRPC Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `jscience_grpc_calls_total` | Counter | Total RPC calls |
| `jscience_grpc_errors_total` | Counter | Failed RPCs |
| `jscience_grpc_latency_seconds` | Histogram | RPC latency |

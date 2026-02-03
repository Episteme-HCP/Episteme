# JScience - Distributed Scientific Computing Platform

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Java](https://img.shields.io/badge/Java-21-orange)]()
[![gRPC](https://img.shields.io/badge/gRPC-1.58-blue)]()
[![License](https://img.shields.io/badge/license-MIT-green)]()

A comprehensive Java platform for distributed scientific computing, featuring:

- **gRPC-based compute grid** with auto-scaling workers
- **Enterprise-grade security** (RBAC, JWT, audit logging)
- **Rich scientific simulations** (N-body, fluid dynamics, fractals, etc.)
- **Modern web dashboard** (Next.js + Recharts)
- **Kubernetes-ready** deployment

## 🚀 Quick Start

```bash
# Clone and build
git clone https://github.com/your-org/jscience.git
cd jscience
mvn clean install -DskipTests

# Start server
java -jar jscience-server/target/jscience-server.jar

# Start workers (in separate terminals)
java -jar jscience-worker/target/jscience-worker.jar

# Run a client app
java -jar jscience-client/target/jscience-client.jar MandelbrotApp
```

## 📦 Modules

| Module | Description |
|--------|-------------|
| `jscience-core` | Core math, physics, visualization |
| `jscience-natural` | N-body, thermodynamics, waves |
| `jscience-social` | Agent-based modeling, economics |
| `jscience-server` | gRPC server, scheduling, security |
| `jscience-worker` | Distributed worker node |
| `jscience-client` | JavaFX client applications |
| `jscience-dashboard` | Next.js web dashboard |

## 🖥️ Client Applications

### Scientific Simulations

- **DistributedMandelbrotApp** - Grid-computed fractals
- **DistributedNBodyApp** - Gravitational simulations (Solar System, Galaxy Collision)
- **MonteCarloPiApp** - Visual π estimation
- **FluidSimApp** - Lattice Boltzmann fluid dynamics
- **WaveSimApp** - 2D wave equation solver

### Grid Tools

- **GridMonitorApp** - Real-time worker/job dashboard
- **WhiteboardApp** - Collaborative drawing canvas
- **DataLakeBrowser** - Streaming data visualization

## 🔧 Server Features

### Enterprise Infrastructure

```
resilience/     - CircuitBreaker, RetryExecutor, HeartbeatMonitor, CheckpointManager
pipeline/       - StreamProcessor, ResultAggregator, DataPartitioner
scheduling/     - PriorityQueueManager, TaskDependencyGraph, AffinityScheduler
security/       - RateLimiterInterceptor, AuditLogger
observability/  - TracingInterceptor, MetricsExporter
performance/    - ConnectionPool, BatchProcessor
workflow/       - WorkflowEngine
```

### gRPC Services

- `ComputeService` - Task submission and management
- `AuthService` - JWT authentication
- `CollaborationService` - Real-time collaboration
- `DataService` - Streaming data pipelines

## 📊 Monitoring

### Prometheus Metrics

```
jscience_tasks_total        - Total tasks submitted
jscience_tasks_completed    - Completed tasks
jscience_workers_active     - Active worker count
jscience_queue_depth        - Current queue size
jscience_task_duration_seconds - Task duration histogram
```

### Grafana Dashboard

Import `jscience-server/src/main/resources/grafana-dashboard.json`

## ☸️ Kubernetes Deployment

```bash
# Deploy to Kubernetes
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/server-deployment.yaml
kubectl apply -f k8s/worker-deployment.yaml
kubectl apply -f k8s/monitoring.yaml
kubectl apply -f k8s/ingress.yaml

# Check status
kubectl get pods -n jscience
```

### Auto-Scaling

Workers auto-scale based on:

- CPU utilization (target: 70%)
- Queue depth (target: 100 tasks per worker)

## 🔐 Security

### Authentication

```java
// Login
AuthResponse response = authStub.login(LoginRequest.newBuilder()
    .setUsername("user")
    .setPassword("password")
    .build());
String token = response.getToken();

// Attach to subsequent calls
Metadata headers = new Metadata();
headers.put(Metadata.Key.of("authorization", ASCII_STRING_MARSHALLER), "Bearer " + token);
```

### Role-Based Access Control

```java
@RequiresRole({"ADMIN", "SCIENTIST"})
public void submitTask(TaskRequest request, StreamObserver<TaskResponse> observer) {
    // Only admins and scientists can submit tasks
}
```

## 🌐 Web Dashboard

```bash
cd jscience-dashboard
npm install
npm run dev
# Open http://localhost:3000
```

Features:

- Real-time worker status
- Task queue monitoring
- Performance charts
- Task submission form

## 📈 Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                               │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │
│  │ JavaFX   │ │ Next.js  │ │ Python   │ │ CLI      │       │
│  │ Apps     │ │ Dashboard│ │ Client   │ │ Tool     │       │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘       │
└───────┼────────────┼────────────┼────────────┼──────────────┘
        │            │            │            │
        └────────────┴────────────┴────────────┘
                          │ gRPC/REST
        ┌─────────────────┴─────────────────┐
        │         JScience Server           │
        │  ┌─────────────────────────────┐  │
        │  │ ComputeService │ AuthService│  │
        │  ├─────────────────────────────┤  │
        │  │ Scheduler │ WorkflowEngine  │  │
        │  ├─────────────────────────────┤  │
        │  │ RBAC │ RateLimit │ Audit    │  │
        │  └─────────────────────────────┘  │
        └─────────────────┬─────────────────┘
                          │
        ┌─────────────────┴─────────────────┐
        │            Workers                │
        │  ┌──────┐ ┌──────┐ ┌──────┐      │
        │  │ W1   │ │ W2   │ │ Wn   │ ...  │
        │  └──────┘ └──────┘ └──────┘      │
        └───────────────────────────────────┘
```

## 📄 License

MIT License - See [LICENSE](LICENSE) for details.

---

**Built with ❤️ by Silvere Martin-Michiellot and Gemini AI (Google DeepMind)**

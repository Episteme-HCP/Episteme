# JScience REST API Documentation

## Overview

JScience Server exposes a REST API for distributed scientific computing. The API provides endpoints for authentication, task submission, server monitoring, and metrics.

**Base URL**: `http://localhost:8080` (configurable via `server.rest.port`)

---

## Authentication

All protected endpoints require a JWT token in the `Authorization` header:

```
Authorization: Bearer <jwt-token>
```

---

## Endpoints

### Health Check

```
GET /api/health
```

**Response** (200 OK):

```json
{
  "status": "healthy",
  "grpc": "localhost:9090",
  "timestamp": 1704214800000
}
```

---

### Server Status

```
GET /api/status
```

**Response** (200 OK):

```json
{
  "activeWorkers": 5,
  "queuedTasks": 10,
  "timestamp": 1704214800000
}
```

---

### List Workers

```
GET /api/workers
```

**Response** (200 OK):

```json
{
  "workers": [
    {"id": "worker-1", "status": "ACTIVE", "cpus": 8},
    {"id": "worker-2", "status": "ACTIVE", "cpus": 16}
  ]
}
```

---

### Login

```
POST /api/auth/login
Content-Type: application/json
```

**Request**:

```json
{
  "username": "scientist@example.com",
  "password": "secret"
}
```

**Response** (200 OK):

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 3600
}
```

---

### Register

```
POST /api/auth/register
Content-Type: application/json
```

**Request**:

```json
{
  "username": "newuser@example.com",
  "password": "securepassword"
}
```

**Response** (201 Created):

```json
{
  "message": "User registered successfully"
}
```

---

### Submit Task

```
POST /api/tasks
Content-Type: application/json
Authorization: Bearer <token>
```

**Request**:

```json
{
  "taskType": "COMPUTE",
  "taskClass": "org.jscience.compute.MandelbrotTask",
  "params": {
    "width": 1920,
    "height": 1080,
    "maxIterations": 1000
  }
}
```

**Response** (200 OK):

```json
{
  "taskId": "task-abc123",
  "status": "QUEUED",
  "timestamp": 1704214800000
}
```

---

### Prometheus Metrics

```
GET /metrics
```

**Response** (200 OK, text/plain):

```
# HELP jscience_tasks_submitted_total Total tasks submitted
# TYPE jscience_tasks_submitted_total counter
jscience_tasks_submitted_total 42.0

# HELP jscience_api_worldbank_calls_total Total World Bank API calls
# TYPE jscience_api_worldbank_calls_total counter
jscience_api_worldbank_calls_total 15.0

# HELP jvm_memory_used_bytes JVM memory used
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{area="heap"} 1.23456789E8
```

---

## Error Responses

All error responses follow this format:

```json
{
  "error": "Error message here"
}
```

| Status Code | Meaning |
|-------------|---------|
| 400 | Bad Request - Invalid JSON or missing fields |
| 401 | Unauthorized - Missing or invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Endpoint not found |
| 405 | Method Not Allowed |
| 500 | Internal Server Error |

---

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `server.rest.port` | 8080 | REST API port |
| `server.grpc.port` | 9090 | gRPC backend port |
| `jwt.secret` | (generated) | JWT signing key |
| `jwt.expiration.hours` | 24 | Token expiration |

---

## Rate Limiting

Rate limiting can be enabled via configuration:

```properties
performance.rate.limiting.enabled=true
```

When enabled, API calls are limited to prevent abuse.

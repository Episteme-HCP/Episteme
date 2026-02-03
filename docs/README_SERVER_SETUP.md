# JScience Server & Infrastructure Setup

This guide details how to set up, build, and run the JScience distributed computing environment, including the central server, database, worker nodes, and client applications.

## 1. Prerequisites

* **Java Development Kit (JDK)**: Version 21 or higher.
* **Apache Maven**: Version 3.9 or higher.
* **Docker & Docker Compose**: (Optional) For running the PostgreSQL database and full stack deployment.

## 2. Quick Start (Docker Compose)

The easiest way to run the entire stack (Server, Database, Workers, Monitoring) is via Docker Compose.

```bash
# Start all services (Postgres, Server, 5 Workers, Prometheus, Grafana)
docker compose up -d

# View logs
docker compose logs -f server
```

* **Server**: gRPC `localhost:50051`, REST `localhost:8080/api`
* **Prometheus**: `localhost:9090`
* **Grafana**: `localhost:3000` (User/Pass: `admin`/`jscience`)

---

## 3. Database Setup

### Option A: H2 (Default / Development)

By default, the server uses an embedded H2 database (file-based). No external setup is required. Data is stored in `./data`.

### Option B: PostgreSQL (Production)

To use PostgreSQL (recommended for production):

1. **Start PostgreSQL**:

    ```bash
    docker compose up -d postgres
    ```

    This starts PostgreSQL 16 on port 5432 (User: `admin`, Pass: `SecureP@ssw0rd123!`, DB: `jscience`).

2. **Configure Server**: Set environment variables when running the server.

    ```bash
    export DATABASE_DRIVER_CLASS=org.postgresql.Driver
    export DATABASE_JOBS_URL=jdbc:postgresql://localhost:5432/jscience
    export DATABASE_USERS_URL=jdbc:postgresql://localhost:5432/jscience
    export DATABASE_USERNAME=admin
    export DATABASE_PASSWORD=SecureP@ssw0rd123!
    ```

---

## 4. Manual Setup (Build & Run)

If you prefer running components manually (without Docker):

### A. Build the Project

```bash
mvn clean install -DskipTests
```

### B. JScience Server (`jscience-server`)

The central coordinator and API gateway.

* **Run**:

    ```bash
    java -jar jscience-server/target/jscience-server-1.0.0-SNAPSHOT.jar
    ```

* **Configuration**:
  * Config file: `jscience-server/src/main/resources/application.properties`
  * **Env Vars**:
    * `PORT`: gRPC port (default: 50051)
    * `JWT_SECRET`: Signing key for tokens (Required in PROD)
    * `MLFLOW_TRACKING_URI`: URL for MLflow (default: <http://localhost:5000>)

### C. Worker Node (`jscience-worker`)

A compute node that connects to the server to execute tasks.

* **Run**:

    ```bash
    # Set Server Host/Port
    export SERVER_HOST=localhost
    export SERVER_PORT=50051
    
    java -jar jscience-worker/target/jscience-worker-1.0.0-SNAPSHOT.jar
    ```

* You can run multiple instances of the worker in separate terminals.

### D. Client Applications (`jscience-client`)

Example distributed applications (Fractals, N-Body, Molecular Dynamics).

* **Run**:

    ```bash
    java -jar jscience-client/target/jscience-client-1.0.0-SNAPSHOT.jar
    ```

* Select the application to run from the CLI menu.

---

## 5. Configuration Reference

Key environment variables managed by `ApplicationConfig.java`:

| Variable | Default | Description |
| :--- | :--- | :--- |
| `SERVER_GRPC_PORT` | `50051` | gRPC server port |
| `SERVER_REST_PORT` | `8080` | REST API port |
| `JWT_SECRET` | `CHANGE_ME...` | Secret for JWT generation |
| `DATABASE_DIR` | `./data` | Directory for H2 DB files |
| `OFFLINE_MODE` | `false` | If true, disables external API calls (WorldBank, etc.) |
| `MLFLOW_TRACKING_URI` | `http://localhost:5000` | MLflow server URL |

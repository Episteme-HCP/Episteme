# JScience Server

The **JScience Server** is the central coordinator for the Distributed Grid system. It manages computational tasks, data feeds, real-time collaboration sessions, and remote device control.

## Features

### 1. Compute Grid (High-Performance Computing)

- **Job Scheduling**: Distributed tasks are queued and dispatched to connected Worker Nodes.
- **Priority Queue**: Uses `PriorityBlockingQueue` to manage execution order.
  - `CRITICAL` (3): Emergency tasks (e.g., real-time collision avoidance).
  - `HIGH` (2): Urgent analysis.
  - `NORMAL` (1): Standard batch processing.
  - `LOW` (0): Background maintenance or low-priority simulation.
- **Fault Tolerance**: Automatic task reassignment if a worker disconnects (Planned).

### 2. Data Lake (Streaming Big Data)

- **Streaming APIs**: Efficiently stream large scientific datasets via gRPC without downloading the entire file.
- **Supported Datatypes**:
  - `StreamGenomeData`: Large-scale DNA sequencing data.
  - `StreamStarCatalog`: Astronomical objects (Position, Magnitude, Type).

### 3. Real-Time Collaboration

- **Session Management**: Users can create and join collaborative sessions (e.g., Shared Whiteboard, Chat).
- **Event Bus**: Publish-Subscribe architecture broadcasts events (`DRAW`, `CURSOR_MOVE`) to all active session participants with low latency.

### 4. Remote Device Control (IoT/Instrumentation)

- **Device Abstraction**: Uniform API to control telescopes, sensors, and robotic arms.
- **Telemetry**: Real-time streaming of sensor data (`DeviceData`) including binary payloads (images) and numeric metrics.
- **Command & Control**: Synchronous `SendCommand` RPCs with parameter maps.

### 5. Enterprise Security & Management (Planned/In-Progress)

- **mTLS Security**: Mutual TLS authentication ensures only authorized clients and workers can connect.
- **User Management**:
  - Role-Based Access Control (RBAC) via JWT Tokens.
  - Roles: `ADMIN` (Control Devices), `SCIENTIST` (Submit Jobs), `VIEWER` (Read-only).
- **Persistence**: H2/PostgreSQL backend for storing:
  - User Profiles & Credentials.
  - Job History & Audit Logs.
  - Device Configuration.

## Use Cases

### 🔬 High-Energy Physics Simulation

*Scenario*: A lab needs to simulate particle collisions (Monte Carlo) requiring 10,000 CPU hours.

- **Grid Usage**: The `ComputeService` distributes 1,000 tasks to 50 worker nodes.
- **Priority**: Submitted as `NORMAL` priority.
- **Result**: Results are aggregated asynchronously as they complete.

### 🔭 Remote Observatory Control

*Scenario*: An astronomer adjusts a telescope in Chile from a lab in Paris.

- **Device Control**: The `DeviceService` sends `MOVE_X`/`MOVE_Y` commands to the telescope hardware.
- **Data Feed**: The `DataService` streams high-resolution spectral data back in real-time.
- **Collaboration**: Other researchers join a shared session (`CollaborationService`) to view the telescope's target feed.

### 🧬 Genome Sequencing Pipeline

*Scenario*: A biotech company processes terabytes of DNA data.

- **Data Lake**: The `DataService` streams specific gene regions (`StreamGenomeData`) without transferring the full 200GB file.
- **Priority**: Urgent diagnostic jobs are submitted with `CRITICAL` priority, pausing background sequencing tasks.

### 🌐 Multi-User Research Whiteboard

*Scenario*: A global team collaborates on climate models.

- **Collaboration**: All participants join a `CollaborationService` session.
- **Flow**: Scientists draw annotations, share cursors, and broadcast events in real-time.
- **Persistence**: Session history is saved to the database for later review.

## Architecture

- **Protocol**: gRPC (Protobuf 3) over HTTP/2.
- **Communication Patterns**:
  - **Unary**: Simple Request/Response (Submit Task).
  - **Server Streaming**: Data Feeds, Telemetry, Event Subscription.
  - **Client Streaming**: Large File Uploads (Future).
  - **Bidirectional Streaming**: Real-time Chat/Sync (Future).

### Components

1. **ComputeService**: The "Brain" - schedules and dispatches tasks.
2. **DataService**: The "Library" - provides access to massive datasets.
3. **CollaborationService**: The "Meeting Room" - manages human-to-human interaction.
4. **DeviceService**: The "Hands" - interfaces with physical hardware.

## Build & Run

### Prerequisites

- JDK 21+
- Maven 3.9+
- Port `50051` available.
- (Optional) H2 Database (Auto-configured).

### Commands

**Start the Server:**

```bash
# Windows
start-server.bat

# Linux/Mac
./start-server.sh
```

**Run with Security:**

```bash
# Start with mTLS enabled (Requires keystores)
java -Dssl=true -jar jscience-server.jar
```

**Custom Port:**

```bash
# Server on port 9000
java -Dport=9000 -jar jscience-server.jar
# Or via CLI argument
java -jar jscience-server.jar 9000
```

## Worker Deployment

Workers are independent processes that connect to the Grid Server and execute tasks.

### Starting a Worker

```bash
# Windows (connects to localhost:50051)
start-worker.bat

# Custom port
start-worker.bat 9000

# Linux/Mac
./start-worker.sh
```

### Worker Lifecycle

1. **Register**: Worker announces its capabilities (cores, memory) via `registerWorker()`
2. **Poll**: Worker continuously requests tasks via `requestTask()`
3. **Execute**: Deserializes the `Callable`, runs it, serializes result
4. **Submit**: Returns result via `submitResult()`

### Scaling Strategies

| Strategy | Description |
|----------|-------------|
| **Horizontal** | Deploy multiple worker instances on different machines |
| **Vertical** | Increase cores/memory per worker |
| **Container** | Use Docker (example: `docker run jscience-worker --server grid.example.com`) |
| **Auto-Discovery** | Workers find server via mDNS (see below) |

### Auto-Discovery (mDNS)

When enabled, workers can automatically discover the server on the local network without specifying an IP address.

```bash
# Server broadcasts its presence
java -Ddiscovery=true -jar jscience-server.jar

# Worker discovers server automatically
java -Ddiscovery=true -jar jscience-worker.jar
```

## API Reference (Protobuf)

### Priority Levels

```protobuf
enum Priority {
  LOW = 0;
  NORMAL = 1;
  HIGH = 2;
  CRITICAL = 3;
}
```

### Data Feeds

- `rpc StreamGenomeData (GenomeRegionRequest) returns (stream GenomeChunk)`
- `rpc StreamStarCatalog (SkyRegionRequest) returns (stream StarObject)`

### Collaboration

- `rpc CreateSession (CreateSessionRequest) returns (SessionResponse)`
- `rpc JoinSession (SessionRequest) returns (stream SessionEvent)`
- `rpc PublishEvent (SessionEvent) returns (PublishAck)`

### Device Control

- `rpc ListDevices (Empty) returns (DeviceList)`
- `rpc SendCommand (DeviceCommand) returns (CommandResponse)`
- `rpc SubscribeTelemetry (DeviceIdentifier) returns (stream DeviceData)`

# JScience Library Dependencies & Weight Analysis

This document provides a technical breakdown of the `launchers/lib` directory, distinguishing between the lightweight requirements for the **Master Demo Suite** and the heavyweight engines required for **Standalone Applications**.

## Directory Overview: `launchers/lib`

The `launchers/lib` directory serves as a standalone repository for runtime dependencies. It allows JScience applications to be executed without Maven, using direct `java` commands.

- **Total Files**: 210
- **Total Weight**: **~370 MB**

---

## 1. Core "Bare" JScience Suite (~42 MB)

To run the `JScienceDemoApp` (Master Demo) and its 30+ interactive simulations, only about **11%** of the total library weight is required.

| Component | Approx. Weight | Description |
| :--- | :--- | :--- |
| **JScience Modules** | 12.6 MB | Compiled JARs for `core`, `natural`, and `social`. |
| **JavaFX Runtime** | 9.5 MB | UI framework for all visual demonstrations. |
| **Measure & Math Core** | 5.2 MB | JSR-385 (Indriya), Vecmath, and Commons-Math. |
| **Infrastructure** | 8.5 MB | Jackson (JSON), Guava, SLF4J/Logback. |
| **Visualization** | 6.2 MB | JFreeChart and basic UI utilities. |

> [!TIP]
> If distributing a lightweight version of the project focused on the interactive demos, you can safely remove the "Heavy Engines" listed below.

---

## 2. Heavy Application Engines (~328 MB)

The remaining **89%** of the weight is comprised of industrial-grade engines used by specific "Killer Apps" (e.g., the World Bank Data Loader, GPU Simulations, or Distributed Models).

### Scientific Computing & GPU (~128 MB)

- **`nd4j-native` (~91 MB)**: High-performance tensor operations and linear algebra.
- **`OpenBLAS` (~28 MB)**: Optimized BLAS/LAPACK implementation for Windows x64.
- **`jcuda` / `jocl` (~8 MB)**: N-body and particle simulation GPU acceleration.

### Big Data & Storage (~110 MB)

- **`RocksDB` (~59 MB)**: High-speed key-value storage for large scientific datasets.
- **`Apache Spark` (~18 MB)**: Distributed processing for massive social/geographical models.
- **`Apache Hadoop` (~19 MB)**: Required runtime for Spark's filesystem interaction.
- **`Avro` / `Parquet` (~14 MB)**: Binary data serialization formats.

### Networking & Microservices (~16 MB)

- **`gRPC` & `Protobuf`**: High-performance RPC framework for remote data services.
- **`Javalin` / `Jetty`**: Embedded web server and WebSocket support.

### Compression & System (~42 MB)

- **`Zstandard` / `Snappy` / `LZ4`**: Various high-speed compression algorithms.
- **`Netty` Native**: Native transport optimizations for networking.
- **`Zookeeper`**: Coordination for distributed systems.

---

## 3. Runtime Requirements

### Java Version

JScience 1.0.0-SNAPSHOT requires **JDK 21** or higher.

### JVM Arguments

When running outside of Maven, ensure the following modules are added:

```bash
java --module-path "lib/javafx" --add-modules javafx.controls,javafx.graphics,javafx.fxml ...
```

For GPU-accelerated modules, the following incubator module may be required:

```bash
--add-modules jdk.incubator.vector
```

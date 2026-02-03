# JScience Performance Tuning Guide

This guide documents JVM startup parameters and optimization strategies to maximize JScience API performance for production deployments.

---

## Quick Start

### Windows (PowerShell/CMD)

```batch
java -server ^
     -XX:+UseG1GC ^
     -XX:MaxGCPauseMillis=200 ^
     -XX:+ParallelRefProcEnabled ^
     -XX:+UseStringDeduplication ^
     -XX:+AlwaysPreTouch ^
     -Xms2G -Xmx4G ^
     -cp "jscience-natural/target/classes;jscience-core/target/classes;jscience-social/target/classes;lib/*;target/dependency/*" ^
     org.jscience.JScience
```

### Linux/macOS (Bash)

```bash
java -server \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+ParallelRefProcEnabled \
     -XX:+UseStringDeduplication \
     -XX:+AlwaysPreTouch \
     -Xms2G -Xmx4G \
     -cp "jscience-natural/target/classes:jscience-core/target/classes:jscience-social/target/classes:lib/*:target/dependency/*" \
     org.jscience.JScience
```

---

## JVM Startup Parameters Explained

### Server Mode

```
-server
```

Enables the Server JIT compiler optimized for long-running applications. Provides better peak performance at the cost of slightly longer startup time.

### Garbage Collection (G1GC)

| Parameter | Description |
|-----------|-------------|
| `-XX:+UseG1GC` | Use G1 Garbage Collector (default in JDK 11+, optimized for low latency) |
| `-XX:MaxGCPauseMillis=200` | Target maximum GC pause time of 200ms |
| `-XX:+ParallelRefProcEnabled` | Process reference objects in parallel during GC |
| `-XX:+UseStringDeduplication` | Deduplicate identical strings to reduce memory footprint |
| `-XX:+AlwaysPreTouch` | Pre-touch all heap pages at startup (reduces latency spikes) |

### Memory Configuration

| Parameter | Description | Recommendation |
|-----------|-------------|----------------|
| `-Xms2G` | Initial heap size | Set equal to `-Xmx` for predictable performance |
| `-Xmx4G` | Maximum heap size | Adjust based on available RAM |
| `-XX:MetaspaceSize=256M` | Initial metaspace size | Larger for applications with many classes |
| `-XX:MaxMetaspaceSize=512M` | Maximum metaspace | Prevent OOM from class loading |

---

## Workload-Specific Configurations

### Scientific Computation (Matrix/Numerical)

```bash
java -server \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=50 \
     -XX:+UseStringDeduplication \
     -XX:+AlwaysPreTouch \
     -XX:+UseNUMA \
     -XX:+UseLargePages \
     -Xms8G -Xmx8G \
     --add-modules jdk.incubator.vector \
     -cp "$CLASSPATH" org.jscience.JScience
```

**Key additions:**

- `-XX:+UseNUMA` - Optimize for NUMA architectures (multi-socket servers)
- `-XX:+UseLargePages` - Use huge pages for better TLB efficiency
- `--add-modules jdk.incubator.vector` - Enable Vector API for SIMD operations

### Astronomy/Physics Simulations

```bash
java -server \
     -XX:+UseZGC \
     -XX:+ZGenerational \
     -Xms4G -Xmx16G \
     -XX:+AlwaysPreTouch \
     -XX:SoftMaxHeapSize=12G \
     --add-modules jdk.incubator.vector \
     -cp "$CLASSPATH" org.jscience.ui.physics.astronomy.SolarSystemSimulation
```

**Key additions:**

- `-XX:+UseZGC` - Z Garbage Collector for ultra-low latency (<1ms pauses)
- `-XX:+ZGenerational` - Generational ZGC (JDK 21+)
- `-XX:SoftMaxHeapSize` - Soft limit before aggressive GC

### Web API/Server Mode

```bash
java -server \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=100 \
     -XX:+ParallelRefProcEnabled \
     -XX:+UseStringDeduplication \
     -XX:+AlwaysPreTouch \
     -XX:+OptimizeStringConcat \
     -XX:+UseCompressedOops \
     -Xms2G -Xmx4G \
     -Djava.net.preferIPv4Stack=true \
     -cp "$CLASSPATH" org.jscience.server.JScienceServer
```

### Memory-Constrained Environments

```bash
java -server \
     -XX:+UseSerialGC \
     -Xms512M -Xmx1G \
     -XX:+UseCompressedOops \
     -XX:+UseCompressedClassPointers \
     -cp "$CLASSPATH" org.jscience.JScience
```

---

## Native Performance (GPU/SIMD)

### Enable Vector API (SIMD)

```bash
java --add-modules jdk.incubator.vector \
     -Dorg.jscience.backend=vector \
     -cp "$CLASSPATH" org.jscience.JScience
```

### GPU Acceleration (OpenCL/CUDA)

```bash
java -Dorg.jscience.backend=gpu \
     -Djava.library.path=./lib/native \
     -cp "$CLASSPATH" org.jscience.JScience
```

See `GPU_SETUP.md` for detailed GPU configuration.

---

## JIT Compilation Tuning

### Aggressive Optimization

```bash
-XX:+TieredCompilation
-XX:TieredStopAtLevel=4
-XX:CompileThreshold=1000
-XX:+UseCodeCacheFlushing
-XX:ReservedCodeCacheSize=512M
```

### Ahead-of-Time Compilation (GraalVM)

```bash
native-image --no-fallback \
    -H:+ReportExceptionStackTraces \
    -cp "$CLASSPATH" \
    org.jscience.JScience
```

---

## Profiling & Monitoring

### Enable JFR (Java Flight Recorder)

```bash
java -XX:+FlightRecorder \
     -XX:StartFlightRecording=duration=60s,filename=jscience.jfr \
     -cp "$CLASSPATH" org.jscience.JScience
```

### Enable JMX Remote Monitoring

```bash
java -Dcom.sun.management.jmxremote \
     -Dcom.sun.management.jmxremote.port=9010 \
     -Dcom.sun.management.jmxremote.authenticate=false \
     -Dcom.sun.management.jmxremote.ssl=false \
     -cp "$CLASSPATH" org.jscience.JScience
```

### GC Logging (JDK 11+)

```bash
java -Xlog:gc*:file=gc.log:time,uptime:filecount=5,filesize=10M \
     -cp "$CLASSPATH" org.jscience.JScience
```

---

## Launcher Scripts

### run_optimized.bat (Windows)

```batch
@echo off
setlocal

set JAVA_OPTS=-server ^
    -XX:+UseG1GC ^
    -XX:MaxGCPauseMillis=200 ^
    -XX:+ParallelRefProcEnabled ^
    -XX:+UseStringDeduplication ^
    -XX:+AlwaysPreTouch ^
    -Xms2G -Xmx4G ^
    --add-modules jdk.incubator.vector

set CLASSPATH=jscience-core/target/classes;jscience-natural/target/classes;jscience-social/target/classes;target/dependency/*

java %JAVA_OPTS% -cp "%CLASSPATH%" org.jscience.ui.JScienceDashboard
```

### run_optimized.sh (Linux/macOS)

```bash
#!/bin/bash

JAVA_OPTS="-server \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -XX:+ParallelRefProcEnabled \
    -XX:+UseStringDeduplication \
    -XX:+AlwaysPreTouch \
    -Xms2G -Xmx4G \
    --add-modules jdk.incubator.vector"

CLASSPATH="jscience-core/target/classes:jscience-natural/target/classes:jscience-social/target/classes:target/dependency/*"

java $JAVA_OPTS -cp "$CLASSPATH" org.jscience.ui.JScienceDashboard
```

---

## Memory Sizing Guidelines

| Application Type | Recommended Heap | Notes |
|------------------|------------------|-------|
| Dashboard/UI | 1-2 GB | Light visualization |
| Simulations (Solar System) | 4-8 GB | Real-time physics |
| Large Datasets (Astronomy) | 8-16 GB | Star catalogs, ephemerides |
| Matrix Operations | 4-16 GB | Depends on matrix size |
| Server/API | 2-4 GB | Per instance |

---

## Troubleshooting

### OutOfMemoryError: Java heap space

```bash
# Increase max heap
-Xmx8G
```

### OutOfMemoryError: Metaspace

```bash
# Increase metaspace
-XX:MaxMetaspaceSize=1G
```

### Long GC Pauses

```bash
# Switch to ZGC for low latency
-XX:+UseZGC -XX:+ZGenerational
```

### Slow Startup

```bash
# Use CDS (Class Data Sharing)
java -Xshare:dump  # First, create archive
java -Xshare:on -cp "$CLASSPATH" org.jscience.JScience
```

---

## Benchmarking

Run benchmarks with optimized settings:

```bash
java -server \
     -XX:+UseG1GC \
     -Xms4G -Xmx4G \
     -XX:+AlwaysPreTouch \
     --add-modules jdk.incubator.vector \
     -cp "jscience-benchmarks/target/classes:$CLASSPATH" \
     org.jscience.benchmarks.MatrixBenchmark
```

Compare with baseline:

```bash
java -cp "jscience-benchmarks/target/classes:$CLASSPATH" \
     org.jscience.benchmarks.MatrixBenchmark
```

---

## References

- [JVM Tuning Guide (Oracle)](https://docs.oracle.com/en/java/javase/21/gctuning/)
- [G1 Garbage Collector](https://docs.oracle.com/en/java/javase/21/gctuning/garbage-first-g1-garbage-collector1.html)
- [ZGC Documentation](https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html)
- [Vector API (Incubator)](https://openjdk.org/jeps/469)
- [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)

# JScience Benchmark Report

**Date**: 2025-11-28 16:14 CET  
**Version**: 1.0
**Status**: PRELIMINARY

## 1. Methodology

We utilize **JMH (Java Microbenchmark Harness)** to rigorously measure the performance of critical mathematical operations.

### 1.1 Metrics
*   **Throughput**: Operations per second (ops/s).
*   **Average Time**: Time per operation (ms/op).
*   **Warmup**: 5 iterations to allow JIT optimization.
*   **Measurement**: 5 iterations for statistical significance.

### 1.2 Environment
*   **CPU**: Intel Core i7 / AMD Ryzen 7 (Reference)
*   **GPU**: NVIDIA RTX 3080 (Reference for CUDA backend)
*   **Java**: OpenJDK 21
*   **Heap**: 4GB (`-Xmx4g`)

## 2. Scenarios

### A. Matrix Multiplication (`MatrixBenchmark`)
*   **Small (100x100)**: CPU cache-friendly.
*   **Medium (1000x1000)**: Memory bandwidth bound.
*   **Large (5000x5000)**: Compute bound (Ideal for GPU).

The default pure Java implementation (`DefaultLinearAlgebraProvider`) is optimized for:
*   **Correctness**: Generic `Field<T>` support.
*   **Portability**: Runs anywhere JVM runs.
*   **Small Data**: Zero overhead for small matrices (< 200x200).

### 4.2 GPU Performance
The `CudaLinearAlgebraProvider` (via JCUDA/JOCL) excels when:
*   **Data Size**: N > 1000.
*   **Compute Intensity**: O(N³) operations (Multiplication, Inversion, Decomposition).
*   **Data Transfer**: The cost of moving data to VRAM is amortized by the computation speed.

## 5. How to Run Benchmarks

```bash
# 1. Build the project
mvn clean install -DskipTests

# 2. Run JMH Benchmarks
java -jar target/benchmarks.jar
```

## 6. Recommendations

1.  **Use Default Provider** for general application logic and small datasets.
2.  **Use CudaProvider** for heavy scientific simulations, deep learning, or large-scale data analysis.
3.  **Future Work**: Implement "Auto-Tuning" in `ComputeContext` to switch providers based on input size.

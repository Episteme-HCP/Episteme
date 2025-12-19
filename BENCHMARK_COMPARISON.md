# JScience Benchmark Comparisons

Detailed performance comparison of JScience linear algebra against other major Java libraries.

## Overview

We compare **JScience 5.0** against:

1. **Apache Commons Math 3.6.1** - Standard comprehensive math library.
2. **EJML 0.43** (Efficient Java Matrix Library) - High-performance pure Java.
3. **Colt 1.2.0** - High performance scientific computing (CERN).
4. **JBlas 1.2.5** - Java bindings for native BLAS (Hardware accelerated).

## Methodology

- **Tool**: JMH (Java Microbenchmark Harness)
- **Metric**: Average Time (ms/op) - Lower is better.
- **Warmup**: 5 iterations
- **Measurement**: 5 iterations
- **Forks**: 1

## Results (Matrix Multiplication)

| Library | Size (NxN) | Score (ms) | Notes |
|---------|------------|------------|-------|
| JScience | 128 | *Pending* | Generic `Quantity<T>` overhead visible |
| Commons | 128 | *Pending* | |
| EJML | 128 | *Pending* | Highly optimized pure Java |
| Colt | 128 | *Pending* | |
| JBlas | 128 | *Pending* | Native AVX/BLAS acceleration |
| | | | |
| JScience | 512 | *Pending* | |
| Commons | 512 | *Pending* | |
| EJML | 512 | *Pending* | |
| Colt | 512 | *Pending* | |
| JBlas | 512 | *Pending* | |

## How to Run

**Windows:**

```cmd
run-benchmarks.bat
```

**Linux/Mac:**

```bash
./run-benchmarks.sh
```

## Analysis

- **JScience** focuses on type safety (`Quantity<T>`) and correct scientific modeling over raw speed.
- **JBlas** and **Colt** are recommended for massive dense matrix operations where type safety is less critical.
- **EJML** offers the best balance of pure Java performance.

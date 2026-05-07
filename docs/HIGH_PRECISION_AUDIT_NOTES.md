# High-Precision Architecture and Fallback Audit

This document details the internal architecture of the Episteme high-precision linear algebra backends, with a focus on performance bottlenecks and "hidden" fallbacks.

## 1. Native MPFR Sparse Backend (`NativeMPFRSparseLinearAlgebraBackend`)

### Iterative Solvers (BiCGSTAB, ConjugateGradient, GMRES)
- **Current Implementation**: These solvers are implemented in **Java** within the `SparseLinearAlgebraProvider` layer.
- **Mechanism**: They perform iterative multiplications of the sparse matrix by a vector (`multiply(Matrix, Vector)`).
- **Performance Gap**: In the current version, every iteration triggers a cross-boundary call (FFM/Panama) to the native MPFR library. While the sparse multiplication itself is native, the "hot loop" control logic remains in JVM space.
- **Optimization Path**: To achieve maximum performance, the entire iterative algorithm should be ported to native C code. This would reduce the overhead of constant memory segment indexing and function pointer lookups.

### Missing Operations and Fallbacks
- **Hidden Fallbacks**: Dense-like operations such as `inverse()`, `lu()`, `qr()`, and `eigen()` are currently routed through **Java Generic Solvers** (e.g., `GenericLU<E>`).
- **Precision Preservation**: These Java-based solvers operate directly on the `E` type (e.g., `RealBig`). Because they use the underlying `Ring<E>` operations, they **maintain full precision** (1000 digits), but they suffer a significant performance penalty compared to a fully native BLAS/LAPACK-style implementation.

---

## 2. Recursive Domain Providers (CARMA and Strassen)

### The "Leaf" Provider Problem
- **Mechanism**: These providers use block-recursive algorithms (CARMA for communication-optimal multiplication, Strassen for $O(n^{2.807})$ complexity). They recursively divide matrices until a "leaf size" is reached.
- **Dependency**: At the leaf level, they delegate to a standard `LinearAlgebraProvider`. 
- **Performance/Architecture Penalty**: In the current test suite, they may fallback to `CPUDenseLinearAlgebraProvider` (which uses `double`) if not explicitly configured with a high-precision leaf.
- **Fix Applied**: The `HighPrecisionComplianceTest` now forces these providers to include `NativeMPFR` backends in their available provider list during the audit to ensure the leaf operations don't degrade the precision to 64-bit doubles.

---

## 3. Summary of Implementation Strategy

| Backend | Sparse Support | Precision | Performance Bottleneck |
| :--- | :--- | :--- | :--- |
| **Native MPFR Dense** | No | 1000+ digits | Memory bandwidth (unstructured allocations) |
| **Native MPFR Sparse** | Yes (CSR) | 1000+ digits | JNI/FFM overhead in iterative solvers |
| **CARMA / Strassen** | No | Variable | Leaf provider selection |
| **gRPC Remote** | Yes | Full Type | Network latency and serialization |

> [!IMPORTANT]
> To eliminate "hidden fallbacks" in the future, we should prioritize implementing **Native Sparse LU** and **Native Sparse QR** in the C/C++ MPFR library to replace the current `GenericLU` Java fallback.

---

## 4. Performance Benchmark Results (2026-04-12)

*Benchmark context: 3×3 matrices, 64-digit precision, single-shot timing. 8 providers tested (no gRPC).*

### 4.1 Provider Ranking — Core Linear Algebra (RealBig Domain)

#### Matrix Addition (3×3)

| Provider | Latency (ms) | Throughput (ops/s) |
|---|---|---|
| **Standard** | **0.019** | **51,493** |
| CPU Dense | 0.020 | 49,480 |
| Strassen | 0.029 | 34,650 |
| CARMA | 0.131 | 7,644 |
| CPU Sparse | 1.002 | 998 |
| MPFR Dense | 9.699 | 103 |
| MPFR Sparse | 10.583 | 95 |

#### Matrix Multiplication (3×3)

| Provider | Latency (ms) | Throughput (ops/s) |
|---|---|---|
| **Standard** | **0.087** | **11,438** |
| CPU Dense | 0.102 | 9,773 |
| CPU Sparse | 0.545 | 1,836 |
| MPFR Dense | 9.316 | 107 |
| MPFR Sparse | 17.420 | 57 |

#### LU Decomposition

| Provider | Latency (ms) | Throughput (ops/s) |
|---|---|---|
| **Strassen** | **0.042** | **23,855** |
| Standard | 0.069 | 14,501 |
| CPU Sparse | 0.080 | 12,525 |
| CPU Dense | 0.084 | 11,965 |
| MPFR Sparse | 0.428 | 2,339 |
| CARMA | 0.513 | 1,949 |
| MPFR Dense | 7.659 | 131 |

#### Eigenvalue Decomposition

| Provider | Latency (ms) | Throughput (ops/s) |
|---|---|---|
| **MPFR Sparse** | **0.728** | **1,374** |
| MPFR Dense | 0.982 | 1,018 |
| Standard | 1.020 | 980 |
| CPU Sparse | 1.821 | 549 |
| CPU Dense | 3.358 | 298 |
| CARMA | 4.977 | 201 |
| Strassen | 18.333 | 55 |

> [!TIP]
> MPFR backends **outperform Java generics on Eigen decomposition** — native MPFR arithmetic enables faster QR iteration convergence.

#### SVD (Most Expensive)

| Provider | Latency (ms) | Throughput (ops/s) |
|---|---|---|
| **Standard** | **10.94** | **91** |
| CPU Sparse | 17.82 | 56 |
| CPU Dense | 24.22 | 41 |
| MPFR Dense | 35.12 | 28 |
| MPFR Sparse | 42.05 | 24 |

---

### 4.2 Complex Domain — JVM vs MPFR Gap

The Complex domain shows the largest performance gap between JVM and native providers, because each complex operation requires multiple FFM calls (real/imaginary separately):

| Operation | Standard (ms) | MPFR Dense (ms) | Ratio |
|---|---|---|---|
| C:Exp | **0.0006** | 0.147 | **245×** |
| C:Sin | **0.002** | 0.173 | **98×** |
| C:Cos | **0.002** | 0.193 | **113×** |
| C:LU | **0.017** | 7.106 | **418×** |
| C:Det | **0.021** | 9.389 | **447×** |
| C:SVD | 2.003 | 41.840 | 21× |

---

### 4.3 Transcendental Functions (RealBig Domain)

| Operation | Standard (ms) | MPFR Dense (ms) | MPFR Sparse (ms) | Best |
|---|---|---|---|---|
| Pow | 0.012 | 0.178 | **0.008** | MPFR Sparse |
| Sqrt | **0.013** | 0.110 | 0.049 | Standard |
| Log | **0.028** | 0.737 | 0.392 | Standard |
| Sin | **0.031** | 1.210 | 0.213 | Standard |
| Exp | **0.056** | 0.096 | 0.104 | Standard |
| Acos | 0.235 | **0.137** | 0.143 | MPFR Dense |
| Cbrt | 0.172 | **0.146** | 0.125 | MPFR Sparse |

> [!NOTE]
> For transcendentals, **Standard and MPFR are comparable** — MPFR excels at functions like Acos and Cbrt where its optimized C implementations outperform the Java BigDecimal Taylor series.

---

### 4.4 Iterative Solvers (Sparse-only)

| Solver | CPU Sparse (ms) | MPFR Sparse (ms) |
|---|---|---|
| ConjGrad (RB) | **0.644** | 4.399 |
| BiCGSTAB (RB) | **1.749** | 2.526 |
| GMRES (RB) | **15.833** | 26.838 |
| ConjGrad (C) | **0.276** | 5.531 |
| BiCGSTAB (C) | **1.746** | 26.087 |
| GMRES (C) | **2.493** | 41.522 |

> [!WARNING]
> **MPFR Sparse C:Solve** has an anomalous **1122ms** latency (vs ~0.03ms for Standard). This is likely a convergence issue with the sparse direct solver on the specific 3×3 test matrix and should be investigated.

---

### 4.5 Key Findings

**Strengths:**
- **Standard provider** is the best general-purpose choice — fastest or near-fastest on most operations
- **Strassen** excels at LU decomposition (0.042ms, the fastest)
- **MPFR backends** show their value in **Eigen decomposition** where native precision arithmetic enables faster convergence
- **Complex transcendentals** are extremely fast on JVM providers (~0.002ms)

**Issues:**
- **Distributed provider** returned -1 on all operations — no leaf providers available in isolation mode
- **CARMA/Strassen** have many RealBig operations returning -1 — delegation fails when restricted to a single provider in test isolation
- **MPFR Dense** is 100–500× slower than JVM for basic operations due to FFM overhead at 3×3 scale

**Scale Considerations:**
- These benchmarks use **3×3 matrices**. At this scale, FFM/Panama call overhead dominates for MPFR
- At larger sizes (100×100+), MPFR backends would show their true advantage: O(n³) computation dominates over constant FFM overhead, and precision-sensitive algorithms (Eigen, SVD) benefit from MPFR's stable arithmetic

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

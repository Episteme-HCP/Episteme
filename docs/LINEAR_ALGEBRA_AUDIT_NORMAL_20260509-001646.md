# Episteme Linear Algebra Audit Report (NORMAL)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Apache Commons Math (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| Colt (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| EJML (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| Episteme (CARMA) | CPU (CARMA) | ❌ Fail | 0/40 | 0/40 |
| Episteme (Standard) | CPU (Standard) | ❌ Fail | 0/40 | 0/40 |
| Episteme (Strassen) | CPU (Strassen) | ❌ Fail | 0/40 | 0/40 |
| Episteme CPU (Dense) | CPU (Standard JVM) | ❌ Fail | 0/40 | 0/40 |
| Episteme CPU (Sparse) | Generic JVM | ❌ Fail | 0/43 | 0/43 |
| Episteme CPU Foundation | Generic JVM | ❌ Fail | 0/43 | 0/43 |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ❌ Fail | 0/43 | 0/43 |
| Episteme gRPC Remote (localhost:50051) | Generic JVM | ❌ Fail | 0/43 | 0/43 |
| JBlas (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| ND4J (Native-Dense) | CPU (Native ND4J) | ❌ Fail | 0/40 | N/A |
| Native CPU-BLAS (Complex) | CPU (Native/Panama) | ❌ Fail | N/A | 0/40 |
| Native CPU-BLAS (Real) | CPU (Native/Panama) | ❌ Fail | 0/40 | N/A |
| Native FFM-BLAS (Complex) | CPU (FFM-BLAS) | ❌ Fail | N/A | 0/40 |
| Native FFM-BLAS (Real) | CPU (FFM-BLAS) | ❌ Fail | 0/40 | N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ❌ Fail | 0/40 | 0/40 |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ❌ Fail | 0/43 | 0/43 |
| Native OpenCL Dense Float Backend | Generic JVM | ➕ N/A | N/A | N/A |
| Native OpenCL Sparse Float Backend | Generic JVM | ➕ N/A | N/A | N/A |
| Native SIMD (Complex) | Generic JVM | ❌ Fail | N/A | 0/40 |
| Native SIMD (Real) | Generic JVM | ❌ Fail | 0/40 | N/A |

### Category: Fallback

| Provider | Fallback:cholesky | Fallback:determinant | Fallback:eigen | Fallback:inverse | Fallback:lu | Fallback:qr | Fallback:solve | Fallback:solveTriangular | Fallback:svd |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Colt (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| EJML (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme (CARMA) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Episteme (Standard) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Episteme (Strassen) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Episteme CPU (Dense) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Episteme CPU (Sparse) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme CPU Foundation | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Episteme gRPC Remote (localhost:50051) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| JBlas (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| ND4J (Native-Dense) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native CPU-BLAS (Complex) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native CPU-BLAS (Real) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native FFM-BLAS (Complex) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native FFM-BLAS (Real) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native MPFR Dense Linear Algebra Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native MPFR Sparse Linear Algebra Backend | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT |
| Native OpenCL Dense Float Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native OpenCL Sparse Float Backend | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT |
| Native SIMD (Complex) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native SIMD (Real) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |

### Category: Arithmetic

| Provider | R:Add | R:Mul | R:Scale | R:Sub | R:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Standard) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native MPFR Sparse Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native SIMD (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | R:Det | R:Inv | R:Solve | R:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Standard) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A |
| JBlas (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native SIMD (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Decompositions

| Provider | R:Chol | R:Eigen | R:LU | R:QR | R:SVD | C:Chol | C:Eigen | C:LU | C:QR | C:SVD |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Standard) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A |
| Native SIMD (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | R:Rect:MatVec | R:Rect:Mul | R:Rect:QR | R:Rect:SVD | R:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Standard) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ BufferUnderflowException | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native MPFR Sparse Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Native SIMD (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | R:Tri:LowerSolve | R:Tri:Mul | R:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Standard) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native MPFR Sparse Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native SIMD (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Vec

| Provider | R:Vec:Angle | R:Vec:Cross | R:Vec:Dot | R:Vec:Norm | R:Vec:Normalize | R:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Standard) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native MPFR Sparse Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native SIMD (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | R:Func:Cbrt | R:Func:Cos | R:Func:Cosh | R:Func:Exp | R:Func:Log | R:Func:Pow | R:Func:Sin | R:Func:Sinh | R:Func:Sqrt | R:Func:Tan | R:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Standard) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native SIMD (Real) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Sparse

| Provider | R:Sparse:BiCGSTAB | R:Sparse:ConjGrad | R:Sparse:GMRES | C:Sparse:BiCGSTAB | C:Sparse:ConjGrad | C:Sparse:GMRES |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Other

| Provider | R:C:MatVec | R:MatVec | C:C:MatVec | C:MatVec |
| :--- | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Episteme (Standard) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Episteme (Strassen) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Episteme CPU (Dense) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Episteme CPU (Sparse) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Episteme CPU Foundation | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException |
| JBlas (Optimized) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError |
| Native CPU-BLAS (Real) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError |
| Native FFM-BLAS (Real) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ❌ NoClassDefFoundError |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ❌ NoClassDefFoundError |
| Native SIMD (Real) | ➕ N/A | ❌ NoClassDefFoundError | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Sat May 09 00:19:17 CEST 2026*

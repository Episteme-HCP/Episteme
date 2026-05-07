# Episteme Linear Algebra Audit Report (NORMAL)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Apache Commons Math (Optimized) | Generic JVM | ⚠️ Partial | 25/40 | N/A |
| Colt (Optimized) | Generic JVM | ✅ Ready | 40/40 | N/A |
| EJML (Optimized) | Generic JVM | ⚠️ Partial | 39/40 | N/A |
| Episteme (CARMA) | CPU (CARMA) | ✅ Ready | 40/40 | 40/40 |
| Episteme (Standard) | CPU (Standard) | ✅ Ready | 40/40 | 40/40 |
| Episteme (Strassen) | CPU (Strassen) | ✅ Ready | 40/40 | 40/40 |
| Episteme CPU (Dense) | CPU (Standard JVM) | ✅ Ready | 40/40 | 40/40 |
| Episteme CPU (Sparse) | Generic JVM | ⚠️ Partial | 39/43 | 42/43 |
| Episteme CPU Foundation | Generic JVM | ⚠️ Partial | 24/43 | 26/43 |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ⚠️ Partial | 38/43 | 38/43 |
| Episteme gRPC Remote (localhost:50051) | Generic JVM | ❌ Fail | 0/43 | 0/43 |
| JBlas (Optimized) | Generic JVM | ✅ Ready | 40/40 | N/A |
| ND4J (Native-Dense) | CPU (Native ND4J) | ⚠️ Partial | 25/40 | N/A |
| Native CPU-BLAS (Complex) | CPU (Native/Panama) | ⚠️ Partial | N/A | 38/40 |
| Native CPU-BLAS (Real) | CPU (Native/Panama) | ✅ Ready | 40/40 | N/A |
| Native CUDA Dense Linear Algebra Backend | N/A | 🔘 Disabled | N/A | N/A |
| Native CUDA Sparse Linear Algebra Backend | N/A | 🔘 Disabled | N/A | N/A |
| Native FFM-BLAS (Complex) | CPU (FFM-BLAS) | ⚠️ Partial | N/A | 39/40 |
| Native FFM-BLAS (Real) | CPU (FFM-BLAS) | ✅ Ready | 40/40 | N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ⚠️ Partial | 40/40 | 27/40 |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ⚠️ Partial | 33/43 | 32/43 |
| Native OpenCL Dense Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native OpenCL Sparse Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native SIMD (Complex) | Generic JVM | ⚠️ Partial | N/A | 37/40 |
| Native SIMD (Real) | Generic JVM | ✅ Ready | 40/40 | N/A |

### Category: Arithmetic

| Provider | R:Add | R:Mul | R:Scale | R:Sub | R:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | R:Det | R:Inv | R:Solve | R:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Decompositions

| Provider | R:Chol | R:Eigen | R:LU | R:QR | R:SVD | C:Chol | C:Eigen | C:LU | C:QR | C:SVD |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | R:Rect:MatVec | R:Rect:Mul | R:Rect:QR | R:Rect:SVD | R:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ❌ BufferUnderflowException | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | R:Tri:LowerSolve | R:Tri:Mul | R:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Vec

| Provider | R:Vec:Angle | R:Vec:Cross | R:Vec:Dot | R:Vec:Norm | R:Vec:Normalize | R:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | R:Func:Cbrt | R:Func:Cos | R:Func:Cosh | R:Func:Exp | R:Func:Log | R:Func:Pow | R:Func:Sin | R:Func:Sinh | R:Func:Sqrt | R:Func:Tan | R:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

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
| Episteme CPU (Sparse) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Episteme CPU Foundation | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Other

| Provider | R:C:MatVec | R:MatVec | C:C:MatVec | C:MatVec |
| :--- | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme (Standard) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme (Strassen) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme CPU (Dense) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme CPU (Sparse) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme CPU Foundation | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException |
| JBlas (Optimized) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native CPU-BLAS (Real) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native FFM-BLAS (Real) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native SIMD (Real) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Thu May 07 22:47:21 CEST 2026*

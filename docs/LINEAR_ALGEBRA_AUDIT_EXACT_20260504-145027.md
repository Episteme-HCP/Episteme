# Episteme Linear Algebra Audit Report (EXACT)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Apache Commons Math (Optimized) | Generic JVM | 🔘 Disabled | N/A | N/A |
| Colt (Optimized) | Generic JVM | 🔘 Disabled | N/A | N/A |
| EJML (Optimized) | Generic JVM | 🔘 Disabled | N/A | N/A |
| Episteme (CARMA) | CPU (CARMA) | ⚠️ Partial | 35/40 | 36/40 |
| Episteme (Standard) | CPU (Standard) | ⚠️ Partial | 36/40 | 36/40 |
| Episteme (Strassen) | CPU (Strassen) | ⚠️ Partial | 33/40 | 36/40 |
| Episteme CPU (Dense) | CPU (Standard JVM) | ⚠️ Partial | 36/40 | 36/40 |
| Episteme CPU (Sparse) | Generic JVM | ⚠️ Partial | 35/43 | 34/43 |
| Episteme CPU Foundation | Generic JVM | ⚠️ Partial | 24/43 | 24/43 |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ⚠️ Partial | 33/43 | 31/43 |
| Episteme gRPC Remote (localhost:50051) | Generic JVM | ❌ Fail | 0/43 | 0/43 |
| JBlas (Optimized) | Generic JVM | 🔘 Disabled | N/A | N/A |
| ND4J (Native-Dense) | CPU (Native ND4J) | ➕ N/A | N/A | N/A |
| Native CPU-BLAS (Complex) | CPU (Native/Panama) | ⚠️ Partial | N/A | 15/40 |
| Native CPU-BLAS (Real) | CPU (Native/Panama) | ⚠️ Partial | 11/40 | N/A |
| Native CUDA Sparse Linear Algebra Backend | N/A | 🔘 Disabled | N/A | N/A |
| Native FFM-BLAS (Complex) | CPU (FFM-BLAS) | ⚠️ Partial | N/A | 20/40 |
| Native FFM-BLAS (Real) | CPU (FFM-BLAS) | ➕ N/A | N/A | N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ⚠️ Partial | 31/40 | 20/40 |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ⚠️ Partial | 26/43 | 25/43 |
| Native OpenCL Dense Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native OpenCL Sparse Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native SIMD (Complex) | Generic JVM | ⚠️ Partial | N/A | 20/40 |
| Native SIMD (Real) | Generic JVM | ⚠️ Partial | 17/40 | N/A |

### Category: Arithmetic

| Provider | RB:Add | RB:Mul | RB:Scale | RB:Sub | RB:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Colt (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| EJML (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | RB:Det | RB:Inv | RB:Solve | RB:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Colt (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| EJML (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A |
| JBlas (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native SIMD (Real) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | RB:Rect:MatVec | RB:Rect:Mul | RB:Rect:QR | RB:Rect:SVD | RB:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Colt (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| EJML (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS |
| Native SIMD (Real) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | RB:Tri:LowerSolve | RB:Tri:Mul | RB:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Colt (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| EJML (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A |
| Native SIMD (Real) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Vec

| Provider | RB:Vec:Angle | RB:Vec:Cross | RB:Vec:Dot | RB:Vec:Norm | RB:Vec:Normalize | RB:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Colt (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| EJML (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Episteme (CARMA) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Native SIMD (Real) | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | RB:Func:Cbrt | RB:Func:Cos | RB:Func:Cosh | RB:Func:Exp | RB:Func:Log | RB:Func:Pow | RB:Func:Sin | RB:Func:Sinh | RB:Func:Sqrt | RB:Func:Tan | RB:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Colt (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| EJML (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Sparse

| Provider | RB:Sparse:BiCGSTAB | RB:Sparse:ConjGrad | RB:Sparse:GMRES | C:Sparse:BiCGSTAB | C:Sparse:ConjGrad | C:Sparse:GMRES |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Colt (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| EJML (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Episteme (CARMA) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |
| Episteme CPU Foundation | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException |
| JBlas (Optimized) | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ AssertionError | ❌ AssertionError | ❌ RuntimeException | ❌ AssertionError | ❌ AssertionError | ❌ RuntimeException |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Mon May 04 14:57:06 CEST 2026*

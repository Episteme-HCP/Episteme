# Episteme Universal Linear Algebra Audit Report (NORMAL)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Apache Commons Math (Optimized) | Generic JVM | ✅ Ready | 40/40 | N/A |
| Colt (Optimized) | Generic JVM | ✅ Ready | 40/40 | N/A |
| EJML (Optimized) | Generic JVM | ⚠️ Partial | 39/40 | N/A |
| Episteme (CARMA) | CPU (CARMA) | ✅ Ready | 40/40 | N/A |
| Episteme (Standard) | CPU (Standard) | ✅ Ready | 40/40 | N/A |
| Episteme (Strassen) | CPU (Strassen) | ⚠️ Partial | 37/40 | N/A |
| Episteme CPU (Dense) | CPU (Standard JVM) | ✅ Ready | 40/40 | N/A |
| Episteme CPU (Sparse) | Generic JVM | ⚠️ Partial | 37/43 | N/A |
| Episteme CPU Foundation | Generic JVM | ⚠️ Partial | 34/43 | N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ⚠️ Partial | 37/43 | N/A |
| Episteme gRPC Remote (localhost:50051) | Generic JVM | ⚠️ Partial | 13/43 | N/A |
| JBlas (Optimized) | Generic JVM | ✅ Ready | 40/40 | N/A |
| ND4J (Native Wrapper) | CPU (Native ND4J) | ✅ Ready | 40/40 | N/A |
| Native CPU-BLAS Linear Algebra Backend | CPU (Native/Panama) | ⚠️ Partial | 39/40 | N/A |
| Native CUDA Dense Linear Algebra Backend | N/A | 🔘 Disabled | N/A | N/A |
| Native CUDA Sparse Linear Algebra Backend | N/A | 🔘 Disabled | N/A | N/A |
| Native FFM BLAS Linear Algebra Backend | CPU (FFM-BLAS) | ✅ Ready | 40/40 | N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ⚠️ Partial | 39/40 | N/A |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ⚠️ Partial | 34/43 | N/A |
| Native OpenCL Dense Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native OpenCL Sparse Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native SIMD Linear Algebra Backend | Generic JVM | ⚠️ Partial | 39/40 | N/A |

### Category: Arithmetic

| Provider | R:Add | R:Mul | R:Scale | R:Sub | R:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | R:Det | R:Inv | R:Solve | R:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | R:Rect:MatVec | R:Rect:Mul | R:Rect:QR | R:Rect:SVD | R:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | R:Tri:LowerSolve | R:Tri:Mul | R:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Vec

| Provider | R:Vec:Angle | R:Vec:Cross | R:Vec:Dot | R:Vec:Norm | R:Vec:Normalize | R:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ✅ PASS | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | R:Func:Cbrt | R:Func:Cos | R:Func:Cosh | R:Func:Exp | R:Func:Log | R:Func:Pow | R:Func:Sin | R:Func:Sinh | R:Func:Sqrt | R:Func:Tan | R:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

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
| Episteme CPU (Sparse) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ AssertionError | ❌ AssertionError | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Fri Apr 17 18:05:22 CEST 2026*

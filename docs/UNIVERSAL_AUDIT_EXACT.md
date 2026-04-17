# Episteme Universal Linear Algebra Audit Report (EXACT)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Apache Commons Math (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| Colt (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| EJML (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| Episteme (CARMA) | CPU (CARMA) | ❌ Fail | 0/40 | N/A |
| Episteme (Standard) | CPU (Standard) | ❌ Fail | 0/40 | N/A |
| Episteme (Strassen) | CPU (Strassen) | ❌ Fail | 0/40 | N/A |
| Episteme CPU (Dense) | CPU (Standard JVM) | ❌ Fail | 0/40 | N/A |
| Episteme CPU (Sparse) | Generic JVM | ❌ Fail | 0/43 | N/A |
| Episteme CPU Foundation | Generic JVM | ❌ Fail | 0/43 | N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ❌ Fail | 0/43 | N/A |
| Episteme gRPC Remote (localhost:50051) | Generic JVM | ❌ Fail | 0/43 | N/A |
| JBlas (Optimized) | Generic JVM | ❌ Fail | 0/40 | N/A |
| ND4J (Native Wrapper) | CPU (Native ND4J) | ❌ Fail | 0/40 | N/A |
| Native CPU-BLAS Linear Algebra Backend | CPU (Native/Panama) | ❌ Fail | 0/40 | N/A |
| Native CUDA Dense Linear Algebra Backend | N/A | 🔘 Disabled | N/A | N/A |
| Native CUDA Sparse Linear Algebra Backend | N/A | 🔘 Disabled | N/A | N/A |
| Native FFM BLAS Linear Algebra Backend | CPU (FFM-BLAS) | ❌ Fail | 0/40 | N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ❌ Fail | 0/40 | N/A |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ❌ Fail | 0/43 | N/A |
| Native OpenCL Dense Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native OpenCL Sparse Linear Algebra Backend | Generic JVM | 🔘 Disabled | N/A | N/A |
| Native SIMD Linear Algebra Backend | Generic JVM | ❌ Fail | 0/40 | N/A |

### Category: Arithmetic

| Provider | RB:Add | RB:Mul | RB:Scale | RB:Sub | RB:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | RB:Det | RB:Inv | RB:Solve | RB:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | RB:Rect:MatVec | RB:Rect:Mul | RB:Rect:QR | RB:Rect:SVD | RB:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | RB:Tri:LowerSolve | RB:Tri:Mul | RB:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Vec

| Provider | RB:Vec:Angle | RB:Vec:Cross | RB:Vec:Dot | RB:Vec:Norm | RB:Vec:Normalize | RB:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ➕ N/A | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ Error | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | RB:Func:Cbrt | RB:Func:Cos | RB:Func:Cosh | RB:Func:Exp | RB:Func:Log | RB:Func:Pow | RB:Func:Sin | RB:Func:Sinh | RB:Func:Sqrt | RB:Func:Tan | RB:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Sparse

| Provider | RB:Sparse:BiCGSTAB | RB:Sparse:ConjGrad | RB:Sparse:GMRES | C:Sparse:BiCGSTAB | C:Sparse:ConjGrad | C:Sparse:GMRES |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ Error | ❌ Error | ❌ Error | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native Wrapper) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native CUDA Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native FFM BLAS Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ Error | ❌ Error | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native OpenCL Sparse Linear Algebra Backend | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED | 🔘 DISABLED |
| Native SIMD Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Fri Apr 17 22:11:18 CEST 2026*

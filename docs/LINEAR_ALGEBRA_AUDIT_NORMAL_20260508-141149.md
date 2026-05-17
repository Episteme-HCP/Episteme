# Episteme Linear Algebra Audit Report (NORMAL)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Apache Commons Math (Optimized) | Generic JVM | ➕ N/A | N/A | N/A |
| Colt (Optimized) | Generic JVM | ➕ N/A | N/A | N/A |
| EJML (Optimized) | Generic JVM | ➕ N/A | N/A | N/A |
| Episteme (CARMA) | CPU (CARMA) | ➕ N/A | N/A | N/A |
| Episteme (Standard) | CPU (Standard) | ➕ N/A | N/A | N/A |
| Episteme (Strassen) | CPU (Strassen) | ➕ N/A | N/A | N/A |
| Episteme CPU (Dense) | CPU (Standard JVM) | ➕ N/A | N/A | N/A |
| Episteme CPU (Sparse) | Generic JVM | ➕ N/A | N/A | N/A |
| Episteme CPU Foundation | Generic JVM | ➕ N/A | N/A | N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ➕ N/A | N/A | N/A |
| Episteme gRPC Remote (localhost:50051) | Generic JVM | ➕ N/A | N/A | N/A |
| JBlas (Optimized) | Generic JVM | ➕ N/A | N/A | N/A |
| ND4J (Native-Dense) | CPU (Native ND4J) | ➕ N/A | N/A | N/A |
| Native CPU-BLAS (Complex) | CPU (Native/Panama) | ➕ N/A | N/A | N/A |
| Native CPU-BLAS (Real) | CPU (Native/Panama) | ➕ N/A | N/A | N/A |
| Native FFM-BLAS (Complex) | CPU (FFM-BLAS) | ➕ N/A | N/A | N/A |
| Native FFM-BLAS (Real) | CPU (FFM-BLAS) | ➕ N/A | N/A | N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ➕ N/A | N/A | N/A |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ➕ N/A | N/A | N/A |
| Native SIMD (Complex) | Generic JVM | ➕ N/A | N/A | N/A |
| Native SIMD (Real) | Generic JVM | ➕ N/A | N/A | N/A |

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
| Native SIMD (Complex) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native SIMD (Real) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |

### Category: Other

| Provider | R:CRITICAL | C:CRITICAL |
| :--- | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ➕ N/A | ➕ N/A |
| Episteme (Standard) | ➕ N/A | ➕ N/A |
| Episteme (Strassen) | ➕ N/A | ➕ N/A |
| Episteme CPU (Dense) | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ➕ N/A | ➕ N/A |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ➕ N/A |
| Episteme gRPC Remote (localhost:50051) | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A |
| Native SIMD (Real) | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Fri May 08 14:15:39 CEST 2026*

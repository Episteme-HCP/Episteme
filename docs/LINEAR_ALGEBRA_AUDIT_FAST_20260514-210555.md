# Episteme Linear Algebra Audit Report (FAST)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Apache Commons Math (Optimized) | Generic JVM | ⚠️ Partial | 18/40 | N/A |
| Colt (Optimized) | Generic JVM | ⚠️ Partial | 32/40 | N/A |
| EJML (Optimized) | Generic JVM | ⚠️ Partial | 31/40 | N/A |
| Episteme (CARMA) | CPU (CARMA) | ⚠️ Partial | 20/40 | 39/40 |
| Episteme (Standard) | CPU (Standard) | ⚠️ Partial | 39/40 | 40/40 |
| Episteme (Strassen) | CPU (Strassen) | ⚠️ Partial | 18/40 | 39/40 |
| Episteme CPU (Dense) | CPU (Standard JVM) | ⚠️ Partial | 39/40 | 39/40 |
| Episteme CPU (Sparse) | Generic JVM | ⚠️ Partial | 41/43 | 41/43 |
| Episteme CPU Foundation | Generic JVM | ⚠️ Partial | 26/43 | 27/43 |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ⚠️ Partial | 31/43 | 34/43 |
| JBlas (Optimized) | Generic JVM | ⚠️ Partial | 32/40 | N/A |
| ND4J (Native-Dense) | CPU (Native ND4J) | ➕ N/A | N/A | N/A |
| Native OpenCL Dense Float Backend | Generic JVM | ⚠️ Partial | 21/40 | 8/40 |
| Native OpenCL Sparse Float Backend | Generic JVM | ⚠️ Partial | 7/43 | 3/43 |

### Category: Fallback

| Provider | Fallback:cholesky | Fallback:determinant | Fallback:eigen | Fallback:inverse | Fallback:lu | Fallback:qr | Fallback:solve | Fallback:solveTriangular | Fallback:svd |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Colt (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| EJML (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme (CARMA) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme (Standard) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme (Strassen) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme CPU (Dense) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme CPU (Sparse) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme CPU Foundation | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| JBlas (Optimized) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| ND4J (Native-Dense) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native OpenCL Dense Float Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native OpenCL Sparse Float Backend | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT |

### Category: Arithmetic

| Provider | R:Add | R:Mul | R:Scale | R:Sub | R:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| JBlas (Optimized) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | R:Det | R:Inv | R:Solve | R:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS |

### Category: Decompositions

| Provider | R:Chol | R:Eigen | R:LU | R:QR | R:SVD | C:Chol | C:Eigen | C:LU | C:QR | C:SVD |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ❌ ArrayStoreException | ❌ ArrayStoreException | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ❌ ArrayStoreException | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ CLException | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | R:Rect:MatVec | R:Rect:Mul | R:Rect:QR | R:Rect:SVD | R:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ❌ AssertionError | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ❌ AssertionError | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| JBlas (Optimized) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ❌ AssertionError | ❌ AssertionError | ❌ CLException | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ➕ N/A | ✅ PASS |
| Native OpenCL Sparse Float Backend | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | R:Tri:LowerSolve | R:Tri:Mul | R:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ❌ AssertionError | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ➕ N/A |
| Native OpenCL Sparse Float Backend | ✅ PASS | ➕ N/A | ✅ PASS | ❌ AssertionError | ➕ N/A | ✅ PASS |

### Category: Vec

| Provider | R:Vec:Angle | R:Vec:Cross | R:Vec:Dot | R:Vec:Norm | R:Vec:Normalize | R:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A |
| JBlas (Optimized) | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | R:Func:Cbrt | R:Func:Cos | R:Func:Cosh | R:Func:Exp | R:Func:Log | R:Func:Pow | R:Func:Sin | R:Func:Sinh | R:Func:Sqrt | R:Func:Tan | R:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Apache Commons Math (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Colt (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| EJML (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme (CARMA) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme (Strassen) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| JBlas (Optimized) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

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
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU Foundation | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| JBlas (Optimized) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ✅ PASS | ✅ PASS | ➕ N/A | ❌ ClassCastException | ❌ ClassCastException | ➕ N/A |

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
| Episteme Distributed Linear Algebra Provider (LocalDistributedContext) | ➕ N/A | ✅ PASS | ➕ N/A | ❌ AssertionError |
| JBlas (Optimized) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ✅ PASS | ➕ N/A | ❌ AssertionError |
| Native OpenCL Sparse Float Backend | ➕ N/A | ✅ PASS | ➕ N/A | ❌ AssertionError |

---
*Generated by Universal Audit Engine on Thu May 14 21:07:22 CEST 2026*

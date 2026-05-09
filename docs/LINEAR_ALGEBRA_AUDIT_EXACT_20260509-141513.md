# Episteme Linear Algebra Audit Report (EXACT)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Episteme CPU (Dense) | CPU (Standard JVM) | ✅ Ready | 40/40 | 40/40 |
| Episteme CPU (Sparse) | Generic JVM | ⚠️ Partial | 40/43 | 40/43 |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ⚠️ Partial | 40/40 | 11/40 |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ⚠️ Partial | 29/43 | 8/43 |

### Category: Fallback

| Provider | Fallback:cholesky | Fallback:determinant | Fallback:eigen | Fallback:inverse | Fallback:lu | Fallback:qr | Fallback:solve | Fallback:solveTriangular | Fallback:svd |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Episteme CPU (Sparse) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native MPFR Dense Linear Algebra Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native MPFR Sparse Linear Algebra Backend | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT |

### Category: Arithmetic

| Provider | RB:Add | RB:Mul | RB:Scale | RB:Sub | RB:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ✅ PASS |

### Category: Solvers

| Provider | RB:Det | RB:Inv | RB:Solve | RB:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ❌ AssertionError | ❌ AssertionError |

### Category: Decompositions

| Provider | RB:Chol | RB:Eigen | RB:LU | RB:QR | RB:SVD | C:Chol | C:Eigen | C:LU | C:QR | C:SVD |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ❌ ArrayStoreException |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | RB:Rect:MatVec | RB:Rect:Mul | RB:Rect:QR | RB:Rect:SVD | RB:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ ArrayStoreException | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ➕ N/A | ✅ PASS |

### Category: Tri

| Provider | RB:Tri:LowerSolve | RB:Tri:Mul | RB:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |

### Category: Vec

| Provider | RB:Vec:Angle | RB:Vec:Cross | RB:Vec:Dot | RB:Vec:Norm | RB:Vec:Normalize | RB:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS |

### Category: Func

| Provider | RB:Func:Cbrt | RB:Func:Cos | RB:Func:Cosh | RB:Func:Exp | RB:Func:Log | RB:Func:Pow | RB:Func:Sin | RB:Func:Sinh | RB:Func:Sqrt | RB:Func:Tan | RB:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Episteme CPU (Sparse) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ❌ AssertionError | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ❌ AssertionError |

### Category: Sparse

| Provider | RB:Sparse:BiCGSTAB | RB:Sparse:ConjGrad | RB:Sparse:GMRES | C:Sparse:BiCGSTAB | C:Sparse:ConjGrad | C:Sparse:GMRES |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Episteme CPU (Sparse) | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError |

### Category: Other

| Provider | RB:C:MatVec | RB:MatVec | C:C:MatVec | C:MatVec |
| :--- | :---: | :---: | :---: | :---: |
| Episteme CPU (Dense) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Episteme CPU (Sparse) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ❌ AssertionError |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ❌ AssertionError |

---
*Generated by Universal Audit Engine on Sat May 09 14:16:36 CEST 2026*

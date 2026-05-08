# Episteme Linear Algebra Audit Report (FAST)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Episteme (Standard) | CPU (Standard) | ✅ Ready | 40/40 | 40/40 |
| ND4J (Native-Dense) | CPU (Native ND4J) | ⚠️ Partial | 23/40 | N/A |
| Native CPU-BLAS (Complex) | CPU (Native/Panama) | ⚠️ Partial | N/A | 39/40 |
| Native CPU-BLAS (Real) | CPU (Native/Panama) | ⚠️ Partial | 38/40 | N/A |
| Native FFM-BLAS (Complex) | CPU (FFM-BLAS) | ✅ Ready | N/A | 40/40 |
| Native FFM-BLAS (Real) | CPU (FFM-BLAS) | ✅ Ready | 40/40 | N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ⚠️ Partial | 40/40 | 28/40 |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ⚠️ Partial | 32/43 | 32/43 |
| Native OpenCL Dense Float Backend | Generic JVM | ➕ N/A | N/A | N/A |
| Native OpenCL Sparse Float Backend | Generic JVM | ➕ N/A | N/A | N/A |
| Native SIMD (Complex) | Generic JVM | ⚠️ Partial | N/A | 37/40 |
| Native SIMD (Real) | Generic JVM | ⚠️ Partial | 38/40 | N/A |

### Category: Fallback

| Provider | Fallback:cholesky | Fallback:determinant | Fallback:eigen | Fallback:inverse | Fallback:lu | Fallback:qr | Fallback:solve | Fallback:solveTriangular | Fallback:svd |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| ND4J (Native-Dense) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native CPU-BLAS (Complex) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native CPU-BLAS (Real) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native FFM-BLAS (Complex) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native FFM-BLAS (Real) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native MPFR Dense Linear Algebra Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native MPFR Sparse Linear Algebra Backend | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT |
| Native OpenCL Dense Float Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ⚠️ DEFAULT | ✅ NATIVE |
| Native OpenCL Sparse Float Backend | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT |
| Native SIMD (Complex) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native SIMD (Real) | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |

### Category: Arithmetic

| Provider | R:Add | R:Mul | R:Scale | R:Sub | R:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | R:Det | R:Inv | R:Solve | R:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Decompositions

| Provider | R:Chol | R:Eigen | R:LU | R:QR | R:SVD | C:Chol | C:Eigen | C:LU | C:QR | C:SVD |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | R:Rect:MatVec | R:Rect:Mul | R:Rect:QR | R:Rect:SVD | R:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ❌ BufferUnderflowException | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | R:Tri:LowerSolve | R:Tri:Mul | R:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native-Dense) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Vec

| Provider | R:Vec:Angle | R:Vec:Cross | R:Vec:Dot | R:Vec:Norm | R:Vec:Normalize | R:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | R:Func:Cbrt | R:Func:Cos | R:Func:Cosh | R:Func:Exp | R:Func:Log | R:Func:Pow | R:Func:Sin | R:Func:Sinh | R:Func:Sqrt | R:Func:Tan | R:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native CPU-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native FFM-BLAS (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native SIMD (Real) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Sparse

| Provider | R:Sparse:BiCGSTAB | R:Sparse:ConjGrad | R:Sparse:GMRES | C:Sparse:BiCGSTAB | C:Sparse:ConjGrad | C:Sparse:GMRES |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| ND4J (Native-Dense) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native MPFR Sparse Linear Algebra Backend | ✅ PASS | ✅ PASS | ❌ RuntimeException | ✅ PASS | ✅ PASS | ❌ RuntimeException |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Real) | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Other

| Provider | R:C:MatVec | R:MatVec | C:C:MatVec | C:MatVec |
| :--- | :---: | :---: | :---: | :---: |
| Episteme (Standard) | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| ND4J (Native-Dense) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Native CPU-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native CPU-BLAS (Real) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Native FFM-BLAS (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native FFM-BLAS (Real) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |
| Native MPFR Dense Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ✅ PASS |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native SIMD (Complex) | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native SIMD (Real) | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Fri May 08 23:54:13 CEST 2026*

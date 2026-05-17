# Episteme Linear Algebra Audit Report (FAST)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | Generic JVM | ➕ N/A | N/A | N/A |
| Native CUDA Dense Linear Algebra Float Backend | Generic JVM | ⚠️ Partial | 11/40 | 8/40 |
| Native CUDA Sparse Linear Algebra Double Backend | Generic JVM | ⚠️ Partial | N/A | 2/43 |
| Native CUDA Sparse Linear Algebra Float Backend | Generic JVM | ⚠️ Partial | 3/43 | 2/43 |
| Native OpenCL Dense Double Backend | Generic JVM | ➕ N/A | N/A | N/A |
| Native OpenCL Dense Float Backend | Generic JVM | ⚠️ Partial | 23/40 | 6/40 |
| Native OpenCL Sparse Double Backend | Generic JVM | ⚠️ Partial | N/A | 3/43 |
| Native OpenCL Sparse Float Backend | Generic JVM | ⚠️ Partial | 7/43 | 3/43 |

### Category: Fallback

| Provider | Fallback:cholesky | Fallback:determinant | Fallback:eigen | Fallback:inverse | Fallback:lu | Fallback:qr | Fallback:solve | Fallback:solveTriangular | Fallback:svd |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native CUDA Dense Linear Algebra Float Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native CUDA Sparse Linear Algebra Double Backend | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT |
| Native CUDA Sparse Linear Algebra Float Backend | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT |
| Native OpenCL Dense Double Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native OpenCL Dense Float Backend | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE | ✅ NATIVE |
| Native OpenCL Sparse Double Backend | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT |
| Native OpenCL Sparse Float Backend | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ⚠️ DEFAULT | ✅ NATIVE | ⚠️ DEFAULT | ⚠️ DEFAULT |

### Category: Arithmetic

| Provider | R:Add | R:Mul | R:Scale | R:Sub | R:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ❌ RuntimeException |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ❌ ClassCastException |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | R:Det | R:Inv | R:Solve | R:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ❌ RuntimeException | ❌ ArrayStoreException | ❌ AssertionError | ✅ PASS | ➕ N/A | ❌ ArrayStoreException | ➕ N/A | ✅ PASS |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS |
| Native CUDA Sparse Linear Algebra Float Backend | ➕ N/A | ➕ N/A | ❌ RuntimeException | ✅ PASS | ➕ N/A | ➕ N/A | ❌ RuntimeException | ✅ PASS |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ ClassCastException | ➕ N/A | ➕ N/A | ✅ PASS |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS |

### Category: Decompositions

| Provider | R:Chol | R:Eigen | R:LU | R:QR | R:SVD | C:Chol | C:Eigen | C:LU | C:QR | C:SVD |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ❌ AssertionError | ✅ PASS | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ AssertionError | ✅ PASS | ➕ N/A | ❌ AssertionError | ➕ N/A |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ✅ PASS | ✅ PASS | ❌ CLException | ✅ PASS | ❌ ClassCastException | ❌ ClassCastException | ❌ ClassCastException | ❌ ClassCastException | ❌ ClassCastException |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | R:Rect:MatVec | R:Rect:Mul | R:Rect:QR | R:Rect:SVD | R:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ❌ AssertionError | ❌ AssertionError | ❌ RuntimeException | ❌ RuntimeException | ❌ RuntimeException | ❌ AssertionError | ❌ AssertionError | ❌ AssertionError | ➕ N/A | ❌ RuntimeException |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Float Backend | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ❌ AssertionError | ❌ AssertionError | ❌ CLException | ✅ PASS | ✅ PASS | ❌ AssertionError | ❌ AssertionError | ❌ ClassCastException | ❌ ClassCastException | ❌ ClassCastException |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | R:Tri:LowerSolve | R:Tri:Mul | R:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ➕ N/A | ✅ PASS | ➕ N/A |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ➕ N/A | ❌ AssertionError |
| Native CUDA Sparse Linear Algebra Float Backend | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ✅ PASS | ➕ N/A |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ✅ PASS | ➕ N/A | ✅ PASS | ❌ AssertionError | ➕ N/A | ✅ PASS |

### Category: Vec

| Provider | R:Vec:Angle | R:Vec:Cross | R:Vec:Dot | R:Vec:Norm | R:Vec:Normalize | R:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Float Backend | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError | ✅ PASS | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | R:Func:Cbrt | R:Func:Cos | R:Func:Cosh | R:Func:Exp | R:Func:Log | R:Func:Pow | R:Func:Sin | R:Func:Sinh | R:Func:Sqrt | R:Func:Tan | R:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Sparse

| Provider | R:Sparse:BiCGSTAB | R:Sparse:ConjGrad | R:Sparse:GMRES | C:Sparse:BiCGSTAB | C:Sparse:ConjGrad | C:Sparse:GMRES |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ❌ ClassCastException | ❌ ClassCastException | ➕ N/A |
| Native CUDA Sparse Linear Algebra Float Backend | ❌ RuntimeException | ❌ RuntimeException | ➕ N/A | ❌ ClassCastException | ❌ ClassCastException | ➕ N/A |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ✅ PASS | ➕ N/A |
| Native OpenCL Sparse Float Backend | ✅ PASS | ✅ PASS | ➕ N/A | ❌ ClassCastException | ❌ ClassCastException | ➕ N/A |

### Category: Other

| Provider | R:C:MatVec | R:MatVec | C:C:MatVec | C:MatVec |
| :--- | :---: | :---: | :---: | :---: |
| Native CUDA Dense Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native CUDA Dense Linear Algebra Float Backend | ➕ N/A | ❌ AssertionError | ➕ N/A | ❌ AssertionError |
| Native CUDA Sparse Linear Algebra Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError |
| Native CUDA Sparse Linear Algebra Float Backend | ➕ N/A | ❌ RuntimeException | ➕ N/A | ❌ RuntimeException |
| Native OpenCL Dense Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Float Backend | ➕ N/A | ✅ PASS | ➕ N/A | ❌ AssertionError |
| Native OpenCL Sparse Double Backend | ➕ N/A | ➕ N/A | ➕ N/A | ❌ AssertionError |
| Native OpenCL Sparse Float Backend | ➕ N/A | ✅ PASS | ➕ N/A | ❌ AssertionError |

---
*Generated by Universal Audit Engine on Tue May 12 21:16:54 UTC 2026*

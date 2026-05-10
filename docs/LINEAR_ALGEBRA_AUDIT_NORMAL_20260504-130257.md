# Episteme Linear Algebra Audit Report (NORMAL)

This report summarizes the compliance and feature support for all discovered Linear Algebra backends. Status codes: ✅ PASS, ❌ FAIL, 🔘 DISABLED (Hardware missing), ➕ N/A (Unsupported).

## Global Status Summary

| Provider | Environment | Status | Real Domain | Complex Domain |
| :--- | :--- | :--- | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | GPU (CUDA) | ⚠️ Partial | 5/43 | N/A |
| Native OpenCL Dense Linear Algebra Backend | Generic JVM | ⚠️ Partial | 31/40 | N/A |
| Native OpenCL Sparse Linear Algebra Backend | Generic JVM | ⚠️ Partial | 1/43 | N/A |

### Category: Arithmetic

| Provider | R:Add | R:Mul | R:Scale | R:Sub | R:Trans | C:Add | C:Mul | C:Scale | C:Sub | C:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Solvers

| Provider | R:Det | R:Inv | R:Solve | R:Trace | C:Det | C:Inv | C:Solve | C:Trace |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Rect

| Provider | R:Rect:MatVec | R:Rect:Mul | R:Rect:QR | R:Rect:SVD | R:Rect:Trans | C:Rect:MatVec | C:Rect:Mul | C:Rect:QR | C:Rect:SVD | C:Rect:Trans |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ❌ RuntimeException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ❌ AssertionError | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Tri

| Provider | R:Tri:LowerSolve | R:Tri:Mul | R:Tri:UpperSolve | C:Tri:LowerSolve | C:Tri:Mul | C:Tri:UpperSolve |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | ➕ N/A | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Vec

| Provider | R:Vec:Angle | R:Vec:Cross | R:Vec:Dot | R:Vec:Norm | R:Vec:Normalize | R:Vec:Proj | C:Vec:Angle | C:Vec:Cross | C:Vec:Dot | C:Vec:Norm | C:Vec:Normalize | C:Vec:Proj |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | ❌ CLException | ➕ N/A | ❌ CLException | ❌ CLException | ❌ CLException | ❌ CLException | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Func

| Provider | R:Func:Cbrt | R:Func:Cos | R:Func:Cosh | R:Func:Exp | R:Func:Log | R:Func:Pow | R:Func:Sin | R:Func:Sinh | R:Func:Sqrt | R:Func:Tan | R:Func:Tanh | C:Func:Cbrt | C:Func:Cos | C:Func:Cosh | C:Func:Exp | C:Func:Log | C:Func:Pow | C:Func:Sin | C:Func:Sinh | C:Func:Sqrt | C:Func:Tan | C:Func:Tanh |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |

### Category: Sparse

| Provider | R:Sparse:BiCGSTAB | R:Sparse:ConjGrad | R:Sparse:GMRES | C:Sparse:BiCGSTAB | C:Sparse:ConjGrad | C:Sparse:GMRES |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| Native CUDA Sparse Linear Algebra Backend | ❌ IllegalArgumentException | ❌ IllegalArgumentException | ❌ IllegalArgumentException | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Dense Linear Algebra Backend | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A | ➕ N/A |
| Native OpenCL Sparse Linear Algebra Backend | ➕ N/A | ➕ N/A | ❌ IllegalArgumentException | ➕ N/A | ➕ N/A | ➕ N/A |

---
*Generated by Universal Audit Engine on Mon May 04 13:03:05 UTC 2026*

# High-Precision Compliance Report

| Provider | Environment | RealBig: Add | RealBig: Multiply | RealBig: Solve | Complex: Multiply | Complex: Add | RealBig: Inverse | Transcendental: Exp |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Colt (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (IllegalArgumentException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (IllegalArgumentException) | ❌ N/A |
| Apache Commons Math (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (SingularMatrixException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (SingularMatrixException) | ❌ N/A |
| EJML (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (SingularMatrixException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (SingularMatrixException) | ❌ N/A |
| Episteme CPU (Unified) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (ArithmeticException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| JBlas (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (LapackException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (LapackException) | ❌ N/A |
| Episteme (CARMA) | CPU (CARMA) | ✅ PASS | ❌ N/A | ⚠️ FAIL (ArithmeticException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| Episteme CPU (Dense) | CPU (Standard JVM) | ✅ PASS | ✅ PASS | ⚠️ FAIL (ArithmeticException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| Episteme CPU (Sparse) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (ArrayStoreException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ❌ N/A | ❌ N/A |
| Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |
| Episteme (Standard) | CPU (Standard) | ✅ PASS | ✅ PASS | ⚠️ FAIL (ArithmeticException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| Episteme (Strassen) | CPU (Strassen) | ✅ PASS | ❌ N/A | ⚠️ FAIL (ArithmeticException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| Native CPU-BLAS Linear Algebra Backend | CPU (Native/Panama) | ❌ N/A | ❌ N/A | ❌ N/A | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| Native CUDA Dense Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| Native CUDA Sparse Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| Native BLAS Provider FFM | CPU (FFM-BLAS) | ❌ N/A | ❌ N/A | ⚠️ FAIL (RuntimeException) | ❌ N/A | ❌ N/A | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| Native MPFR Arbitrary-Precision Backend | CPU (Panama/MPFR) | ✅ PASS | ⚠️ FAIL (RuntimeException) | ❌ N/A | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ❌ N/A | ❌ N/A |
| Linear Algebra Provider | CPU (Panama/MPFR/Sparse) | ❌ N/A | ❌ N/A | ❌ N/A | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ❌ N/A | ❌ N/A |
| ND4J (Native Wrapper) | CPU (Native ND4J) | ❌ N/A | ❌ N/A | ⚠️ FAIL (RuntimeException) | ❌ N/A | ❌ N/A | ⚠️ FAIL (RuntimeException) | ❌ N/A |
| Native OpenCL Dense Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| Native OpenCL Sparse Linear Algebra Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| Native SIMD Linear Algebra Backend | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (ArithmeticException) | ❌ N/A | ❌ N/A | ⚠️ FAIL (ArithmeticException) | ❌ N/A |
| gRPC Remote (localhost:50051) | Generic JVM | ⚠️ FAIL (RuntimeException) | ⚠️ FAIL (RuntimeException) | ⚠️ FAIL (RuntimeException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (ClassCastException) | ⚠️ FAIL (RuntimeException) | ❌ N/A |

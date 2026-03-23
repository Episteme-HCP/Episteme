# High-Precision Compliance Report

| Provider | Environment | RealBig: Add | RealBig: Multiply | RealBig: Solve | Complex: Multiply | Complex: Add | RealBig: Inverse | Transcendental: Exp |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Colt (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular.) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular.) | ❌ N/A |
| Apache Commons Math (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (matrix is singular) | ❌ N/A | ❌ N/A | ⚠️ FAIL (matrix is singular) | ❌ N/A |
| EJML (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (Solution contains uncountable numbers) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Solution contains uncountable numbers) | ❌ N/A |
| Episteme CPU (Unified) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (Singular) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular) | ❌ N/A |
| JBlas (Optimized) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (LAPACK DGESV: Linear equation cannot be solved because the matrix was singular.) | ❌ N/A | ❌ N/A | ⚠️ FAIL (LAPACK DGESV: Linear equation cannot be solved because the matrix was singular.) | ❌ N/A |
| Episteme (CARMA) | CPU (CARMA) | ✅ PASS | ❌ N/A | ⚠️ FAIL (Singular) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular) | ❌ N/A |
| Episteme CPU (Dense) | CPU (Standard JVM) | ✅ PASS | ✅ PASS | ⚠️ FAIL (Singular) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular) | ❌ N/A |
| Episteme CPU (Sparse) | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (Cannot invoke "Object.getClass()" because the return value of "org.episteme.core.mathematics.linearalgebra.Vector.getProvider()" is null) | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |
| Distributed Linear Algebra Provider (LocalDistributedContext) | Generic JVM | ❌ N/A | ⚠️ FAIL (MPFR Sparse multiply failed) | ⚠️ FAIL (MPFR Conjugate Gradient failed) | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |
| Episteme (Standard) | CPU (Standard) | ✅ PASS | ✅ PASS | ⚠️ FAIL (Singular) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular) | ❌ N/A |
| Episteme (Strassen) | CPU (Strassen) | ✅ PASS | ❌ N/A | ⚠️ FAIL (Singular) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular) | ❌ N/A |
| Native CPU-BLAS Linear Algebra Backend | CPU (Native/Panama) | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular) | ❌ N/A |
| Native CUDA Dense Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| CUDA Sparse Linear Algebra Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| Native BLAS Provider FFM | CPU (FFM-BLAS) | ❌ N/A | ❌ N/A | ⚠️ FAIL (FFM Solve failed) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular (U(2,2) is exactly zero). Cannot compute inverse.) | ❌ N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ⚠️ FAIL (MPFR scale failed) | ⚠️ FAIL (MPFR multiply failed: handle's method type (MemorySegment,MemorySegment,int,int)int but found (MemorySegment,MemorySegment,int,int)void) | ⚠️ FAIL (MPFR solve failed) | ⚠️ FAIL (class org.episteme.core.mathematics.numbers.complex.Complex cannot be cast to class org.episteme.core.mathematics.numbers.real.Real (org.episteme.core.mathematics.numbers.complex.Complex and org.episteme.core.mathematics.numbers.real.Real are in unnamed module of loader 'app')) | ⚠️ FAIL (class org.episteme.core.mathematics.numbers.complex.Complex cannot be cast to class org.episteme.core.mathematics.numbers.real.Real (org.episteme.core.mathematics.numbers.complex.Complex and org.episteme.core.mathematics.numbers.real.Real are in unnamed module of loader 'app')) | ⚠️ FAIL (MPFR inverse failed) | ❌ N/A |
| Native MPFR Sparse Linear Algebra Backend | CPU (Panama/MPFR/Sparse) | ❌ N/A | ⚠️ FAIL (MPFR Sparse multiply failed) | ⚠️ FAIL (MPFR Conjugate Gradient failed) | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |
| ND4J (Native Wrapper) | CPU (Native ND4J) | ❌ N/A | ❌ N/A | ⚠️ FAIL (ND4J Solve Operation Failed) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Op [matrix_inverse] execution failed) | ❌ N/A |
| Native OpenCL Dense Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| Native OpenCL Sparse Linear Algebra Backend | DISABLED | N/A | N/A | N/A | N/A | N/A | N/A | N/A |
| Native SIMD Linear Algebra Backend | Generic JVM | ❌ N/A | ❌ N/A | ⚠️ FAIL (Singular matrix) | ❌ N/A | ❌ N/A | ⚠️ FAIL (Matrix is singular) | ❌ N/A |
| gRPC Remote (localhost:50051) | Generic JVM | ⚠️ FAIL (Remote server is unavailable at localhost:50051. Check if the Episteme server is running.) | ⚠️ FAIL (Remote server is unavailable at localhost:50051. Check if the Episteme server is running.) | ⚠️ FAIL (Remote server is unavailable at localhost:50051. Check if the Episteme server is running.) | ⚠️ FAIL (class org.episteme.core.mathematics.numbers.complex.Complex cannot be cast to class org.episteme.core.mathematics.numbers.real.Real (org.episteme.core.mathematics.numbers.complex.Complex and org.episteme.core.mathematics.numbers.real.Real are in unnamed module of loader 'app')) | ⚠️ FAIL (class org.episteme.core.mathematics.numbers.complex.Complex cannot be cast to class org.episteme.core.mathematics.numbers.real.Real (org.episteme.core.mathematics.numbers.complex.Complex and org.episteme.core.mathematics.numbers.real.Real are in unnamed module of loader 'app')) | ⚠️ FAIL (Remote server is unavailable at localhost:50051. Check if the Episteme server is running.) | ❌ N/A |

*Generated by HighPrecisionComplianceTest on Sun Mar 23 20:58:23 CET 2026*

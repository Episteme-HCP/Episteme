# Episteme Linear Algebra Isolated Compliance Report

| Provider | Environment | Transpose | Multiply | Inverse | LU | QR | SVD | Eigen | Solve | SpMV | CG | BiCGSTAB | GMRES | SpMV (Complex) | BiCGSTAB (Complex) |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Native BLAS Provider FFM | CPU (FFM-BLAS) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |

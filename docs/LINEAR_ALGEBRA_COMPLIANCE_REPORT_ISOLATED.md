# Episteme Linear Algebra Isolated Compliance Report

| Provider | Environment | Transpose | Multiply | Inverse | LU | QR | SVD | Solve |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ⚠️ FAIL (RuntimeException) | ✅ PASS |
| Native MPFR Dense Linear Algebra Backend | CPU (Panama/MPFR) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ⚠️ FAIL (RuntimeException) | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | Generic JVM | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ⚠️ FAIL (RuntimeException) | ✅ PASS |

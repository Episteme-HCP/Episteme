# Episteme Linear Algebra Isolated Compliance Report

| Provider | Environment | Transpose | Multiply | Inverse | LU | QR | SVD | Solve |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Native CPU-BLAS Linear Algebra Backend | CPU (Native/Panama) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| ND4J (Native Wrapper) | CPU (Native ND4J) | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS | ✅ PASS |
| Native MPFR Arbitrary-Precision Backend | CPU (Panama/MPFR) | ✅ PASS | ✅ PASS | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ✅ PASS |
| Native MPFR Sparse Linear Algebra Backend | CPU (Panama/MPFR/Sparse) | ✅ PASS | ✅ PASS | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |
| Native OpenCL Sparse Linear Algebra Backend | GPU (OpenCL) | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A | ❌ N/A |

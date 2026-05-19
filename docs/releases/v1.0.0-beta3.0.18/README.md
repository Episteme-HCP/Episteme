# Episteme v1.0.0-beta3 Release Documentation

## Overview
Episteme v1.0.0-beta3 marks a significant milestone in the stabilization of the native scientific computing kernel. This release focuses on memory safety, thread-concurrency, and high-performance linear algebra backends.

## Key Improvements
- **Memory Safety**: Migrated from `Arena.ofConfined()` to `Arena.ofAuto()` for all native storage implementations, ensuring safe cross-thread access within `TaskExecutor` pools.
- **Numerical Stability**: Resolved complex conjugation argument ordering in BLAS `CDOTC`/`ZDOTC` calls and implemented dynamic variant detection for struct-returning native functions.
- **Backend Optimization**: Verified performance parity and gains for the Native FFM BLAS backend across all precision modes.
- **Audit Infrastructure**: Established a robust, multimodal cloud audit pipeline (AWS) for continuous performance validation.

## Performance Audit Results (AWS)
The following reports were generated on an AWS EC2 instance (admin@3.250.201.241) after applying the stability fixes.

### Summary Metrics (NORMAL Mode)
| Provider | R:Vec:Dot (ops/s) | R:MatVec (ops/s) | R:Add (ops/s) |
| :--- | :--- | :--- | :--- |
| **Native FFM BLAS** | **5970.15** | **2233.09** | **1026.66** |
| Episteme CPU (Dense) | 4726.34 | 1123.80 | 1619.62 |
| JBlas (Optimized) | 1445.19 | 707.47 | 99.87 |
| Apache Commons Math | 489.86 | 821.23 | 332.54 |

### Artifacts
- [Performance Audit (NORMAL)](artifacts/performance_audit_normal.pdf)
- [Performance Audit (FAST)](artifacts/performance_audit_fast.pdf)
- [Performance Audit (EXACT)](artifacts/performance_audit_exact.pdf)
- [Raw JSON Data (NORMAL)](artifacts/performance_audit_normal.json)

## Verification
The release was verified via:
1. **Remote Cloud Audit**: Full execution of the benchmark suite on AWS with zero `IllegalStateException` or `SIGSEGV` crashes.
2. **Local Stability Test**: Multithreaded stress testing of the `NativeFFMBLASBackend` using `Arena.ofAuto()`.
3. **Linkage Check**: Confirmation of successful native symbol binding across different BLAS implementations (OpenBLAS, MKL).

## Deployment Instructions
To deploy the native scientific computing kernel:
1. Ensure JDK 25 is installed.
2. Run `./install_native_libs.sh` to extract the platform-specific binaries.
3. Configure `LD_LIBRARY_PATH` to include the `./libs` directory.

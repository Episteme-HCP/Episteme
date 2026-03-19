# Native & GPU Safety Audit (March 19, 2026)

## Security & Reliability Assessment

### CUDA Backend (Panama / FFM)
- **Status**: 🔴 UNSTABLE (VM Regression Risk)
- **Primary Issue**: Unchecked address handling via `MemorySegment.ofAddress(long)`.
- **Finding**: Native addresses are passed between GPU and Host without adequate lifecycle management. This leads to segmentation faults if a handle is reused after a VM crash or improper `cudaFree`.

### OpenCL Backend (JOCL / JNI)
- **Status**: 🟡 VULNERABLE (Device Incompatibility)
- **Primary Issue**: `fp64` Extension Missing
- **Finding**: Most gaming-grade GPUs or mobile integrated GPUs lack `cl_khr_fp64`. The backend currently crashes on initialization if the extension is not supported instead of falling back gracefully.

## Critical Safety Gaps

1. **Process Isolation**: Native calls execute in the same process as the JVM. A single segfault crashes the entire Episteme server/client.
2. **Memory Leaks**: `cudaMalloc` allocations are not automatically tracked by the JVM GC. Manual `cudaFree` is required in `finally` blocks, but exceptions in `finally` can lead to untracked memory growth.

## Improvement Strategy

> [!CAUTION]
> **Priority 1: Native Boundary Protection**
> Implement a `NativeSafe` wrapper for all `MethodHandle` invocations to catch and log native state before execution.

> [!WARNING]
> **Priority 2: Extension Discovery**
> Update `NativeOpenCLDenseLinearAlgebraBackend` to verify `cl_khr_fp64` before attempting kernel compilation.

> [!NOTE]
> **Priority 3: Isolated Testing**
> Develop a Docker-based test harness that runs CUDA/OpenCL tests in a separate container, preventing local developer environment crashes.

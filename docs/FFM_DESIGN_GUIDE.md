# Episteme Native Layer Design Guide: Project Panama (FFM)

This document outlines the standard patterns for implementing native backends in Episteme using the Java Foreign Function & Memory (FFM) API.

## 1. Memory Management Priorities

### Arena Scoping
- **Confined Arena** (`Arena.ofConfined()`): Use for per-operation allocations (e.g., temporary work buffers in QR/SVD). Always use try-with-resources.
- **Shared Arena** (`Arena.ofShared()`): Use for resources accessed by multiple threads or for long-lived backend state.
- **Global Arena** (`Arena.global()`): Use ONLY for library loading or permanent constants that should never be unloaded.

### Data Ownership
- Always prefer `MemorySegment.ofAddress()` for pointers returned by native code.
- Explicitly document if a segment is "host-owned" or "device-owned".

## 2. Library Loading Pattern

All native backends MUST use the unified [NativeLibraryLoader](file:///c:/Silvere/Encours/Developpement/Episteme/episteme-core/src/main/java/org/episteme/core/technical/backend/nativ/NativeLibraryLoader.java#25-361) in `episteme-core`.

```java
Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary("mylib");
if (lib.isPresent()) {
    C_MALLOC = LINKER.downcallHandle(lib.get().find("malloc").get(), ...);
}
```

## 3. Handling Device Synchronicity

Backends interacting with accelerators (GPU, FPGA) must implement the [synchronize()](file:///c:/Silvere/Encours/Developpement/Episteme/episteme-native/src/main/java/org/episteme/nativ/mathematics/linearalgebra/backends/NativeCUDADenseLinearAlgebraBackend.java#1172-1173) hook.

- **Implementation**: Override [shutdown()](file:///c:/Silvere/Encours/Developpement/Episteme/episteme-core/src/main/java/org/episteme/core/technical/backend/nativ/NativeLibraryLoader.java#352-360) to call [synchronize()](file:///c:/Silvere/Encours/Developpement/Episteme/episteme-native/src/main/java/org/episteme/nativ/mathematics/linearalgebra/backends/NativeCUDADenseLinearAlgebraBackend.java#1172-1173) before the JVM exits to ensure all asynchronous results are committed or cleared.

## 4. Provider Metadata

Every [AlgorithmProvider](file:///c:/Silvere/Encours/Developpement/Episteme/episteme-core/src/main/java/org/episteme/core/technical/algorithm/AlgorithmProvider.java#16-80) should expose its capabilities via [getMetadata()](file:///c:/Silvere/Encours/Developpement/Episteme/episteme-core/src/main/java/org/episteme/core/technical/algorithm/AlgorithmProvider.java#70-79). This allows the `ProviderSelector` to optimize for:
- GPU residence requirements.
- Numerical precision constraints (FP16/FP32/FP64).
- Memory footprint thresholds.

lux et robur.

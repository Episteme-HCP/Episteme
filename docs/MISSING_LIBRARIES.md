# Missing Libraries and Dependencies Report

This document outlines the external libraries required for full functionality of JScience's native backends, and details the current status of code related to them.

## 1. TarsosDSP Backend (Audio Analysis)
*   **File:** `org.jscience.core.media.backends.TarsosBackend.java`
*   **Status:** The entire implementation is currently commented out or stubbed.
*   **Source:** [https://github.com/JorenSix/Tarsos](https://github.com/JorenSix/Tarsos) (Tarsos) / [https://github.com/JorenSix/TarsosDSP](https://github.com/JorenSix/TarsosDSP)
*   **Reason:** The required dependency `be.tarsos.dsp` is missing from the project's Maven configuration.
*   **Fix:** Add the TarsosDSP repository and dependency to `pom.xml`.
    ```xml
    <repository>
        <id>tarsos-dsp</id>
        <url>https://mvn.0110.be/releases</url>
    </repository>
    
    <dependency>
        <groupId>be.tarsos.dsp</groupId>
        <artifactId>core</artifactId>
        <version>2.5</version>
    </dependency>
    <dependency>
        <groupId>be.tarsos.dsp</groupId>
        <artifactId>jvm</artifactId>
        <version>2.5</version>
    </dependency>
    ```

## 2. Native Audio (miniaudio / PortAudio)
*   **File:** `org.jscience.nativ.media.audio.backends.NativeAudioBackend.java`
*   **Status:** Implemented using Project Panama (FFM) Handles.
*   **Source:** [https://github.com/mackron/miniaudio](https://github.com/mackron/miniaudio)
*   **Requirement:** The native shared library (`miniaudio.dll`, `libminiaudio.so`, or `libminiaudio.dylib`) must be present in the path or loaded via `java.library.path`.
*   **Commented Code:** Some advanced initialization logic (struct layouts for `ma_decoder_config`) is simplified. The code attempts to load functions dynamically.

## 3. CUDA Backend (GPU Acceleration)
*   **File:** `org.jscience.core.technical.backend.gpu.cuda.CUDABackend.java`
*   **Status:** Implemented with stub methods for memory management (`allocateGPUMemory`, `copyToGPU` return fake handles or print logs).
*   **Requirement:** NVIDIA CUDA Toolkit (v12.0+) installed on the host machine.
*   **Fix:** To make this fully functional, complex `struct` layouts for the CUDA Driver API need to be mapped in Java using `MemoryLayout`.

## 4. Native Tensor Provider (oneDNN / MKL)
*   **File:** `org.jscience.nativ.mathematics.linearalgebra.tensors.providers.NativeTensorProvider.java`
*   **Status:** Implemented with fallbacks.
*   **Source:** [https://github.com/uxlfoundation/oneDNN](https://github.com/uxlfoundation/oneDNN)
*   **Requirement:** Intel MKL (`mkl_rt`) or oneDNN (`dnnl`) libraries installed.
*   **Commented Code:** Several optimization paths (like bulk native set) are marked with TODOs and fall back to Java loops if the native library is missing.

## 5. Native Physics (Bullet3 / Jolt)
*   **Status:** Not yet implemented.
*   **Plan:** Create `NativePhysicsProvider` using FFM to wrap Bullet Physics C-API.
*   **Target Libs:** `C:\JScience-Native\bullet3.dll` (or similar)

## 6. Native Data (Arrow / Parquet)
*   **Status:** Not yet implemented.
*   **Plan:** Create `NativeArrowProvider` wrapping Apache Arrow C Data Interface.
*   **Target Libs:** `C:\JScience-Native\hdf5.dll` (as hinted by user context)

## 7. Native Quantum (QuEST / Qiskit Aer)
*   **Status:** Not yet implemented.
*   **Plan:** Create `NativeQuantumProvider` wrapping QuEST or Qiskit Aer via FFM.


# JScience Native Library Setup Guide

This guide provides instructions for acquiring and setting up the native libraries required for full hardware acceleration and extended functionality in JScience.

## Missing Libraries Overview

Some high-performance backends require external native binaries (`.dll` on Windows) that are not bundled by default due to size or licensing.

### 1. Apache Arrow (`arrow.dll`)
Required for high-speed columnar data processing and Arrow-based linear algebra.

**Option A: Download Official Binaries**
1. Visit the [Apache Arrow Installation Page](https://arrow.apache.org/install/).
2. Look for "C++" or "vcpkg" instructions for Windows.
3. Alternatively, check the [C++ binary releases](https://arrow.apache.org/release/) for pre-compiled DLLs.

**Option B: Extract from PyArrow (Recommended for Devs)**
If you have Python installed:
1. Run `pip install pyarrow`.
2. Locate the `pyarrow` directory in your Python `site-packages`.
3. You will find `arrow.dll` (and others like `parquet.dll`) inside. Copy them to the `libs/` or `target/` directory of your JScience project.

---

### 2. QuEST (`quest.dll`)
Required for the high-performance Quantum Exact Simulation Toolkit backend.

**Compilation Required**
QuEST does not provide official pre-compiled DLLs. You must compile it from source:
1. Clone the repository: `git clone https://github.com/QuEST-Kit/QuEST.git`
2. Install **CMake** and a C compiler (e.g., **MinGW-w64** or **Visual Studio Build Tools**).
3. Run the following commands in the QuEST directory:
   ```powershell
   mkdir build
   cd build
   cmake .. -DCMAKE_BUILD_TYPE=Release
   cmake --build . --config Release
   ```
4. Find the resulting `QuEST.dll` (or `libQuEST.dll`) and rename it to `quest.dll` if necessary, then place it in the JScience library path.

---

### 3. VLC Media Player (`vlcj`)
Required for video and advanced audio processing (AudioViewerDemo).

**System Installation**
1. Download and install the latest **VLC Media Player** (64-bit) from [videolan.org](https://www.videolan.org/vlc/).
2. Ensure VLC (v3.0.0 or higher) is installed. The `vlcj` library will automatically discover the installation on your system PATH or standard installation directories.

---

### 4. CUDA Acceleration
Required for specialized GPU-accelerated computing.

1. Ensure you have an NVIDIA GPU.
2. Install the **NVIDIA CUDA Toolkit** (v11.8 or v12.x recommended) from [NVIDIA's website](https://developer.nvidia.com/cuda-downloads).
3. JScience will automatically attempt to locate the CUDA runtime (`nvcuda.dll`) and BLAS (`cublas64_*.dll`) libraries.

---

## Deployment Location
Copy the acquired `.dll` files to any of the following locations for automatic discovery:
- Project root `/libs/` (e.g., `c:\Silvere\Encours\Developpement\JScience\libs\`)
- System PATH
- The directory containing the application JAR

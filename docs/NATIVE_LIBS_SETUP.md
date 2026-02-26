# JScience Native Library Setup Guide

JScience uses high-performance native backends for linear algebra, physics simulation, and data processing. This guide provides instructions for acquiring and configuring these libraries across Windows, Linux, and macOS.

## 1. Directory Structure

For a portable installation, place all native libraries in the project's root `libs/` directory.

### Minimal `libs/` Checklist (Windows)

| Component | Required DLLs |
| :--- | :--- |
| **Linear Algebra** | `libopenblas.dll` (or `mkl_rt.dll`) |
| **FFT** | `libfftw3-3.dll`, `libfftw3f-3.dll`, `libfftw3l-3.dll` |
| **Data I/O** | `arrow.dll`, `parquet.dll`, `hdf5.dll` (+ wrappers) |
| **Physics** | `libbulletc.dll`, `ode.dll` |
| **Quantum** | `quest.dll` |
| **Multimedia** | `libvlc.dll`, `libvlccore.dll` |
| **Runtime** | `msvcp140*.dll`, `vcruntime140*.dll`, `concrt140.dll` |

---

## 2. Installation Guides

### [OpenBLAS](https://github.com/xianyi/OpenBLAS/releases) (Linear Algebra)

- **Windows**: Download the `.zip` from GitHub Releases and extract the DLL.
- **Linux**: `sudo apt install libopenblas-dev` (Debian/Ubuntu) or `sudo dnf install openblas-devel` (Fedora).
- **macOS**: `brew install openblas`.

### [Apache Arrow](https://arrow.apache.org/install/) (Columnar Data)

- **Easiest Path (All OS)**: `pip install pyarrow`. Locate `arrow.dll` (Win), `libarrow.so` (Linux), or `libarrow.dylib` (macOS) in your Python `site-packages/pyarrow`.
- **Windows (vcpkg)**: `vcpkg install arrow`.
- **macOS**: `brew install apache-arrow`.
- **Linux**: Use the official Apache APT/YUM repositories.

### [HDF5](https://www.hdfgroup.org/downloads/hdf5/) (Scientific Data)

- **Windows**: Download the `.msi` or `.zip` installer from The HDF Group.
- **Linux**: `sudo apt install libhdf5-dev`.
- **macOS**: `brew install hdf5`.

### [FFTW3](http://www.fftw.org/download.html) (Fast Fourier Transform)

- **Windows**: Download pre-compiled 64-bit DLLs from the official site.
- **Linux/macOS**: Standard package managers (`fftw3`).

### [QuEST](https://github.com/QuEST-Kit/QuEST) (Quantum Simulation)

1. Clone the repository and build from source using CMake:
   ```bash
   mkdir build; cd build; cmake ..; cmake --build . --config Release
   ```
2. Copy the resulting binary to `libs/quest`.

### [VLC Media Player](https://www.videolan.org/vlc/) (Multimedia)

- **Setup**: Install the official VLC application. JScience will detect it automatically via `vlcj`.
- **Bundling**: Copy `libvlc` and `libvlccore` to `libs/`.

### [CUDA](https://developer.nvidia.com/cuda-downloads) (GPU Acceleration)

1. Install the CUDA Toolkit from NVIDIA.
2. Ensure `BIN` directory is in `PATH`.
3. (Optional) Copy `cudart64_*.dll` and `cublas64_*.dll` to `libs/` for portability.

---

## 3. Configuration & Paths

### JVM Argument
Ensure the native library path is set when running the application:
`-Djava.library.path=libs`

### Launcher Scripts
The provides scripts in the `launchers/` directory handle path resolution automatically using relative paths.

---

## 4. Verification

Run the verification tool to check if all backends are correctly detected:
- **Windows**: `.\launchers\run-verify.bat`
- **Linux/macOS**: `./launchers/run-verify.sh`

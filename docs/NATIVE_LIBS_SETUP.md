# Episteme Native Library Setup Guide

Episteme utilizes high-performance native backends for computation, physics, audio, and data I/O. This guide provides comprehensive instructions for acquiring, installing, and configuring these libraries across Windows, Linux, and macOS.

---

## 🚀 Quick Start: The `libs/` Directory

Episteme is configured to look for native libraries in the project's root `libs/` directory. For a portable installation, place all relevant `.dll` (Windows), `.so` (Linux), or `.dylib` (macOS) files in this folder.

**JVM Argument:**
`-Djava.library.path=libs` (This is handled automatically by the provided launchers).

---

## 🛠️ Library Directory & Installation Methods

### 1. Linear Algebra & Deep Learning (BLAS/LAPACK/oneDNN)

| Library | Purpose | Download / Installation |
| :--- | :--- | :--- |
| **OpenBLAS** | CPU Acceleration | **Win**: [Releases](https://github.com/OpenMathLib/OpenBLAS/releases) (copy `libopenblas.dll`) <br> **Lin**: `sudo apt install libopenblas-dev` <br> **Mac**: `brew install openblas` |
| **Intel MKL** | Optimal CPU Perf | **Win/Lin/Mac**: Part of [Intel oneAPI Base Toolkit](https://www.intel.com/content/www/us/en/developer/tools/oneapi/base-toolkit.html) <br> **Pip**: `pip install mkl` |
| **oneDNN** (dnnl) | Deep Learning | **Win/Lin/Mac**: [GitHub Releases](https://github.com/oneapi-src/oneDNN/releases) (Download binary release for your OS) |

### 2. GPU Acceleration (CUDA & OpenCL)

> [!IMPORTANT]
> CUDA requires an NVIDIA GPU and matching drivers.

| Component | Purpose | Installation Method |
| :--- | :--- | :--- |
| **CUDA Toolkit** | Core GPU Support | **Win/Lin**: [NVIDIA CUDA Downloads](https://developer.nvidia.com/cuda-downloads) <br> **Requirement**: Ensure `cublas64_13.dll` (or current version) is in your path or `libs/`. |
| **cuBLAS / cuSPARSE** | Linear Algebra | Included in the CUDA Toolkit. Copy `cublas64_*.dll`, `cusparse64_*.dll` to `libs/`. |
| **OpenCL** | Heterogeneous Computing | **Win**: Included with GPU Drivers. <br> **Lin**: `sudo apt install intel-opencl-icd` or `nvidia-opencl-icd-libopencl1` <br> **Mac**: Built-in (Deprecated by Apple, but still available). |

### 3. Physics & Mechanics (Bullet, ODE)

| Library | Purpose | Installation Method |
| :--- | :--- | :--- |
| **Bullet Physics** | Rigid Body & Collision | **All**: [BulletSharpPInvoke (libbulletc)](https://github.com/AndresTraks/BulletSharpPInvoke) <br> Compile `libbulletc` from source using CMake if binaries are not available for your platform. |
| **ODE** (Open Dynamics) | Articulated Dynamics | **Win**: [BitBucket Downloads](https://bitbucket.org/odedevs/ode/downloads/) (Look for `ode-*.tar.gz`) <br> **Lin**: `sudo apt install libode-dev` <br> **Mac**: `brew install ode` |

### 4. Audio & Multimedia (MiniAudio, PortAudio, VLC)

| Library | Purpose | Installation Method |
| :--- | :--- | :--- |
| **MiniAudio** | Low-latency Audio | **All**: [miniaud.io](https://miniaud.io/) <br> Episteme uses a native wrapper. Ensure `miniaudio.dll` is present in `libs/`. |
| **PortAudio** | Cross-platform Audio | **Win**: [Official Site](http://www.portaudio.com/download.html) <br> **Lin**: `sudo apt install libportaudio2` <br> **Mac**: `brew install portaudio` |
| **VLC (libvlc)** | Media Playback | **Win**: Install [VLC Media Player](https://www.videolan.org/vlc/) and copy `libvlc.dll`, `libvlccore.dll` and the `plugins/` folder. <br> **Lin**: `sudo apt install libvlc-dev` <br> **Mac**: `brew install --cask vlc` |
| **mpg123** | MP3 Decoding | **Win**: [mpg123.org](https://mpg123.de/download/win64/) <br> **Lin**: `sudo apt install libmpg123-dev` <br> **Mac**: `brew install mpg123` |

### 5. Data Formats (Arrow, HDF5, FITS)

| Format | Purpose | Installation Method |
| :--- | :--- | :--- |
| **Apache Arrow** | Columnar Data | **Win**: `vcpkg install arrow` <br> **Lin**: `sudo apt install libarrow-dev` <br> **Mac**: `brew install apache-arrow` |
| **HDF5** | Large Data Arrays | **Win**: [HDF Group Downloads](https://www.hdfgroup.org/downloads/hdf5/) <br> **Lin**: `sudo apt install libhdf5-dev` <br> **Mac**: `brew install hdf5` |
| **CFITSIO** (FITS) | Scientific Images | **Win**: [NASA HEASARC](https://heasarc.gsfc.nasa.gov/fitsio/) <br> **Lin**: `sudo apt install libcfitsio-dev` <br> **Mac**: `brew install cfitsio` |

### 6. Quantum & Parallelism (QuEST, MPJ, HDFS)

| Library | Purpose | Installation Method |
| :--- | :--- | :--- |
| **QuEST** | Quantum Simulation | **All**: [GitHub](https://github.com/quest-kit/QuEST) <br> Clone and compile using CMake: `cmake -D GPUACCEL=OFF ..` |
| **MPJ Express** | Message Passing (MPI) | **All**: [MPJ Express Site](http://mpj-express.org/) <br> Download the TAR/ZIP, unpack, and set `MPJ_HOME`. |
| **HDFS Native** | Hadoop IO | **Lin**: Part of Hadoop Distro (`libhadoop.so`). <br> **Win**: Requires community builds of `winutils.exe` and `hadoop.dll` (e.g., [cdarlint/winutils](https://github.com/cdarlint/winutils)). |

---

## 🖥️ Platform-Specific Notes

### Windows (Visual C++ Redistributable)
Most native libraries require the **Visual C++ Redistributable for Visual Studio 2015-2022**.
- **Download**: [Official Microsoft Links](https://aka.ms/vs/17/release/vc_redist.x64.exe)

### Linux (LD_LIBRARY_PATH)
On Linux, ensure `libs/` is added to your library path if using system-installed dependencies:
`export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:./libs`

### macOS (Permissions)
Due to Gatekeeper, you may need to manually allow unsigned `.dylib` files:
`xattr -d com.apple.quarantine libs/*.dylib`

---

## 📦 Missing Dependency Checklist (Troubleshooting)

If a library is present but fails to load (`UnsatisfiedLinkError`), it is often missing one of these transitive dependencies. Ensure these are also in your `libs/` folder or system path:

### For `libsndfile.dll` (Audio)
- [ ] `libFLAC.dll`
- [ ] `libvorbis.dll`
- [ ] `libvorbisenc.dll`
- [ ] `libogg.dll`
- [ ] `libopus.dll`
- [ ] `libmp3lame.dll`
- [ ] `libmpg123.dll` (Optional, for MP3 decode)

### For `arrow.dll` / `parquet.dll` (Data I/O)
- [ ] `zlib1.dll` (or `zlib.dll`)
- [ ] `lz4.dll`
- [ ] `zstd.dll`
- [ ] `snappy.dll`
- [ ] `libbrotlicommon.dll`
- [ ] `libbrotlidec.dll`
- [ ] `libutf8proc.dll`

### For `libvlc.dll` (Multimedia)
- [ ] `plugins/` directory (Must be adjacent to the DLLs)

### For `QuEST.dll` / `Bullet` / `ODE` (Scientific)
- [ ] `libgcc_s_seh-1.dll`
- [ ] `libstdc++-6.dll`
- [ ] `libwinpthread-1.dll`

---

## 🔍 Harvesting from Local System (Recommended)

Before downloading from the web, you can often find these missing DLLs in other software installed on your machine. This ensures internal compatibility.

### 📍 Likely Search Paths:
| Software | Path to Check | Potential DLLs Found |
| :--- | :--- | :--- |
| **VLC Media Player** | `C:\Program Files\VideoLAN\VLC` | `plugins/` directory, `libvlccore.dll` |
| **Git (MinGW)** | `C:\Program Files\Git\mingw64\bin` | `zlib1.dll`, `libgcc_s_seh-1.dll`, `libstdc++-6.dll` |
| **Git (Bin)** | `C:\Program Files\Git\usr\bin` | `libiconv.dll`, `liblzma.dll` |
| **MSYS2 / MinGW** | `C:\msys64\mingw64\bin` | `libvorbis.dll`, `libFLAC.dll`, `libogg.dll` |
| **Ananconda / Miniconda**| `...\Anaconda3\Library\bin` | `arrow.dll`, `snappy.dll`, `zstd.dll`, `lz4.dll` |

### 🛠️ Step-by-Step Repatriation:
1. Locate the software directory (e.g., VLC or Git).
2. Search for the missing `.dll` files listed in the checklist above.
3. **Copy (don't move)** the files into the Episteme `libs/` folder.
4. For VLC, copy the entire `plugins/` folder into `libs/`.

---

## ✅ Verification Tools

To verify that Episteme can successfully find and load your native components, use the following launcher:
- **Windows**: `.\launchers\run-verify.bat`
- **Linux/macOS**: `./launchers/run-verify.sh`

---

## ❓ Troubleshooting

- **`UnsatisfiedLinkError`**: This usually means a dependency of the library is missing (e.g., missing `libgfortran` for OpenBLAS). Use `ldd` (Linux), `Dependencies` (Windows), or `otool -L` (macOS) to check for missing transitive dependencies.
- **Wrong Architecture**: Ensure you are using **64-bit (x64)** libraries. Episteme does not support 32-bit (x86) backends.

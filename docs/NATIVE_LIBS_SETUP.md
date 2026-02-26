# JScience Native Library Setup Guide

This guide describes how to set up the native libraries required for full hardware acceleration.

## Minimal `libs/` Directory Checklist

After cleanup, your `libs/` folder should contain **only** these files:

| Component | Required DLLs |
| :--- | :--- |
| **HDF5** | `hdf5.dll`, `hdf5_cpp.dll`, `hdf5_hl.dll`, `hdf5_hl_cpp.dll`, `hdf5_java.dll`, `hdf5_tools.dll` |
| **OpenBLAS** | `libopenblas.dll` |
| **FFTW3** | `libfftw3-3.dll`, `libfftw3f-3.dll`, `libfftw3l-3.dll` |
| **Bullet3** | `libbulletc.dll` |
| **ODE** | `ode.dll` |
| **Tarsos** | `libsndfile.dll` |
| **Runtime** | `msvcp140*.dll`, `vcruntime140*.dll`, `concrt140.dll` |

---

## Setting up Missing Libraries

### 1. Apache Arrow (`arrow.dll`, `arrow_io.dll`)
> [!IMPORTANT]
> The NuGet and Java source packages in your `dl/` folder do **not** contain the native DLLs.

**Easiest Setup:**
1. Open PowerShell and run: `pip install pyarrow`
2. Locate the `pyarrow` folder in your Python installation (e.g., `C:\Users\<User>\AppData\Local\Programs\Python\Python31x\Lib\site-packages\pyarrow`).
3. Copy `arrow.dll` and `arrow_io.dll` into `JScience/libs/`.

---

### 2. CUDA Acceleration
1. Run the installer you downloaded: `cuda_13.1.1_windows.exe`.
3. (Optional) To make the project portable, copy `cudart64_*.dll` and `cublas64_*.dll` from `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v11.x\bin` to `JScience/libs/`.

---

### 3. VLC Media Player

1. Install `vlc-3.0.23-win64.exe`.
2. JScience (via vlcj) will detect the installation automatically.
3. If you want to bundle it, copy `libvlc.dll`, `libvlccore.dll` and the `plugins/` folder to `libs/`.

---

### 4. QuEST (`quest.dll`)
1. Extract `QuEST-main.zip`.
2. Open a terminal in the folder and run:
   ```powershell
   mkdir build; cd build
   cmake .. -G "Visual Studio 17 2022" -A x64
   cmake --build . --config Release
   ```
3. Copy `Release\QuEST.dll` to `JScience/libs/quest.dll`.

---

## Troubleshooting
- **Path**: Ensure `c:\Silvere\Encours\Developpement\JScience\libs` is in your application library path.
- **Architecture**: All DLLs must be **64-bit**.

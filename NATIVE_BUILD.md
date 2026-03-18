# Linux Native Build Requirements

This document outlines the requirements and steps for compiling the native MPFR backend for Episteme on Linux/WSL.

## Prerequisites

You must have a C/C++ development environment installed. On Debian/Ubuntu based systems:

```bash
sudo apt-get update
sudo apt-get install build-essential cmake
```

### Libraries

The following development libraries are required:

- `libgmp-dev`: GNU Multiple Precision Arithmetic Library
- `libmpfr-dev`: Multiple Precision Floating-Point Reliably

```bash
sudo apt-get install libgmp-dev libmpfr-dev
```

## Compilation Steps

1. Navigate to the project root.
2. Ensure `libs` directory exists.
3. Run the build script:

```bash
sh build_mpfr.sh
```

This will produce `libs/libmpfr_jni.so` (or similar depending on the OS).

## Troubleshooting

- **Missing JNI headers**: Ensure `JAVA_HOME` is set correctly and points to a JDK with `include/jni.h`.
- **Linker errors**: Verify that `libmpfr` and `libgmp` are in the system library path or set `LD_LIBRARY_PATH`.

#!/bin/bash
# Episteme - MPFR Build Script for Linux/macOS
# Uses mini-gmp for a standalone high-precision library.

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUILD_DIR="$SCRIPT_DIR/episteme-native/src/main/cpp/mpfr_build/build"
LIBS_DIR="$SCRIPT_DIR/libs"

echo "[INFO] Starting MPFR build for Linux/macOS..."

mkdir -p "$BUILD_DIR"
mkdir -p "$LIBS_DIR"

cd "$BUILD_DIR"

# Configure with CMake
# We assume 'cmake' and 'gcc'/'clang' are available.
cmake .. -DCMAKE_BUILD_TYPE=Release

# Build
cmake --build . --config Release

# Copy results to libs/
if [ -f "libmpfr.so" ]; then
    cp "libmpfr.so" "$LIBS_DIR/"
    echo "[INFO] Success! libmpfr.so copied to $LIBS_DIR"
elif [ -f "libmpfr.dylib" ]; then
    cp "libmpfr.dylib" "$LIBS_DIR/"
    echo "[INFO] Success! libmpfr.dylib copied to $LIBS_DIR"
else
    echo "[ERROR] libmpfr.so/dylib not found in $BUILD_DIR"
    exit 1
fi

echo "[INFO] MPFR build complete."

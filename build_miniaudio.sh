#!/bin/bash
# build_miniaudio.sh
# Compiles libminiaudio.so and copies it to the root libs/ directory.

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
BUILD_DIR="$SCRIPT_DIR/episteme-native/build_miniaudio"
SOURCE_DIR="$SCRIPT_DIR/episteme-native/src/main/cpp/audio"
OUTPUT_DIR="$SCRIPT_DIR/libs"

echo "Cleaning build directory..."
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"
cd "$BUILD_DIR"

echo "Configuring CMake..."
cmake "$SOURCE_DIR" -DCMAKE_BUILD_TYPE=Release

echo "Building miniaudio..."
cmake --build .

echo "Consolidating binary to libs/..."
mkdir -p "$OUTPUT_DIR"
cp *.so "$OUTPUT_DIR/" 2>/dev/null || cp *.dylib "$OUTPUT_DIR/" 2>/dev/null

echo "[SUCCESS] miniaudio (Linux/Mac) build complete."

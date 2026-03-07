#!/bin/bash
# Episteme Vision C++ Build Script for Linux
# Builds libepisteme-native.so using CMake

BUILD_DIR="episteme-native/build_vision_linux"
SOURCE_DIR="episteme-native/src/main/cpp"
OUTPUT_DIR="episteme-native/libs"

echo "Cleaning build directory..."
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"
cd "$BUILD_DIR"

echo "Configuring CMake..."
# Resolve absolute path for source dir
ABS_SOURCE_DIR=$(cd "../../$SOURCE_DIR" && pwd)
cmake "$ABS_SOURCE_DIR" -DCMAKE_BUILD_TYPE=Release

echo "Building Release..."
cmake --build . --config Release

cd ../..

echo "Copying libepisteme-native.so to libs..."
mkdir -p "$OUTPUT_DIR"
cp "$BUILD_DIR/libepisteme-native.so" "$OUTPUT_DIR/"

echo "[SUCCESS] Episteme Vision (Linux) build complete."

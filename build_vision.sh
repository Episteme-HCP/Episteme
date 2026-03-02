#!/bin/bash
# Episteme Vision C++ Build Script for Linux
# Builds libepisteme_vision.so using CMake

mkdir -p episteme-native/src/main/cpp/build
cd episteme-native/src/main/cpp/build

cmake ..
make

# Copy to central libs directory
mkdir -p ../../../../libs
cp libepisteme_vision.so ../../../../libs/

echo "[SUCCESS] Episteme Vision (Linux) build complete."

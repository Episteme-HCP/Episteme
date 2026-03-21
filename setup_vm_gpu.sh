#!/bin/bash
# Episteme GPU VM Setup Script (Reproducibility)
# This script installs JDK 25, CUDA/OpenCL dependencies, and MPFR on a Debian/Ubuntu GPU VM.

set -e

echo "--- [1/4] Installing System Dependencies ---"
sudo apt update
sudo apt install -y --no-install-recommends \
    wget curl git build-essential cmake pkg-config \
    libvlc-dev libhdf5-dev libfftw3-dev libopenblas-dev portaudio19-dev \
    nvidia-opencl-icd ocl-icd-opencl-dev opencl-headers libmpfr-dev

echo "--- [2/4] Installing OpenJDK 25 ---"
if [ ! -d "$HOME/jdk-25.0.1" ]; then
    wget https://download.java.net/java/early_access/jdk25/11/GPL/openjdk-25-ea+11_linux-x64_bin.tar.gz -O /tmp/jdk25.tar.gz
    tar -xzf /tmp/jdk25.tar.gz -C "$HOME"
    rm /tmp/jdk25.tar.gz
fi

echo "--- [3/4] Configuring Environment Variables ---"
# Add to .bashrc if not already present
if ! grep -q "JAVA_HOME.*jdk-25.0.1" "$HOME/.bashrc"; then
    echo 'export JAVA_HOME=$HOME/jdk-25.0.1' >> "$HOME/.bashrc"
    echo 'export PATH=$JAVA_HOME/bin:$PATH' >> "$HOME/.bashrc"
    echo 'export LD_LIBRARY_PATH=/usr/local/cuda/lib64:$LD_LIBRARY_PATH' >> "$HOME/.bashrc"
fi

# Apply to current session
export JAVA_HOME=$HOME/jdk-25.0.1
export PATH=$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=/usr/local/cuda/lib64:$LD_LIBRARY_PATH

echo "--- [4/4] Verifying Installation ---"
java -version
clinfo | grep -i "Platform Name" || echo "Warning: clinfo not found or failed, but OpenCL might still work."

echo "Setup Complete! Please run 'source ~/.bashrc' or restart your shell."

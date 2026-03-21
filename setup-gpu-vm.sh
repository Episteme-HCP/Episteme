#!/bin/bash
# Episteme GPU VM Configuration Script (Run this ON the VM)
# This script configures JDK 25, CUDA, and OpenCL for ND4J/GPGPU operations.

set -e

echo "--- [0/4] Basic Dependencies & Repositories ---"
# Enable contrib, non-free and non-free-firmware for Debian 12 (Bookworm)
if [ -f /etc/apt/sources.list.d/debian.sources ]; then
    sudo sed -i 's/Components: main/Components: main contrib non-free non-free-firmware/g' /etc/apt/sources.list.d/debian.sources
fi
sudo apt-get update
sudo apt-get upgrade -y
sudo apt-get install -y unzip zip curl git maven build-essential dkms

echo "--- [1/4] JDK 25 Setup (SDKMAN) ---"
if [ ! -d "$HOME/.sdkman" ]; then
    echo "Installing SDKMAN..."
    curl -s "https://get.sdkman.io" | bash
fi

# Load SDKMAN
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install JDK 25 if not present
if ! sdk list java | grep -q "25-open"; then
    echo "Installing JDK 25..."
    sdk install java 25-open
else
    echo "JDK 25 already managed by SDKMAN."
fi
sdk use java 25-open

echo "--- [2/4] NVIDIA & CUDA Drivers ---"
if ! command -v nvidia-smi &> /dev/null; then
    echo "Installing NVIDIA drivers..."
    sudo apt-get update
    sudo apt-get install -y linux-headers-amd64 build-essential
    sudo apt-get install -y nvidia-driver nvidia-cuda-toolkit
else
    echo "[INFO] NVIDIA drivers found."
    nvidia-smi
fi

# --- [3/4] OpenCL & Media Setup ---
echo "Installing OpenCL runtimes and media dependencies..."
sudo apt-get install -y mesa-opencl-icd ocl-icd-opencl-dev clinfo vlc
clinfo | grep -i "platform" || echo "[WARN] OpenCL clinfo check failed. You may need a reboot."

echo "--- [4/4] Project Sync ---"
cd ~
if [ ! -d "Episteme" ]; then
    echo "Cloning repository..."
    git clone https://github.com/silveremartin-dev/Episteme.git
    cd Episteme
else
    echo "Updating repository..."
    cd Episteme
    git fetch origin main
    git reset --hard origin/main
fi

echo ""
echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo "IMPORTANT: If you just installed NVIDIA drivers, REBOOT the VM:"
echo "sudo reboot"
echo ""
echo "After reboot, you can run benchmarks using:"
echo "./run_vm_benchmark_local.sh"
echo "=========================================="

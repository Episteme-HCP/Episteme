#!/bin/bash
# JScience HPC - Native Libraries Installer for Ubuntu/Debian
# This script installs all required native libraries using apt

set -e

echo "=== JScience HPC Native Libraries Installer (Ubuntu/Debian) ==="
echo ""

# Check if running as root
if [ "$EUID" -ne 0 ]; then 
    echo "Please run as root (use sudo)"
    exit 1
fi

# Update package list
echo "Updating package list..."
apt-get update -qq

# Function to install package
install_package() {
    local name=$1
    shift
    local packages=("$@")
    
    echo ""
    echo "--- Installing $name ---"
    apt-get install -y "${packages[@]}"
    echo "✓ $name installed"
}

# Install MPI
install_package "OpenMPI" \
    openmpi-bin openmpi-common libopenmpi-dev

# Install HDF5
install_package "HDF5" \
    libhdf5-dev libhdf5-serial-dev hdf5-tools

# Install CFITSIO
install_package "CFITSIO" \
    libcfitsio-dev libcfitsio-bin

# Install FFTW3
install_package "FFTW3" \
    libfftw3-dev libfftw3-double3 libfftw3-single3

# Install OpenBLAS
install_package "OpenBLAS" \
    libopenblas-dev libopenblas-base

# Install Bullet Physics
install_package "Bullet Physics" \
    libbullet-dev libbullet-extras-dev

# Install build tools (for custom builds)
install_package "Build Tools" \
    build-essential cmake git pkg-config

# Update library cache
echo ""
echo "Updating library cache..."
ldconfig

# Verify installations
echo ""
echo "=== Verification ==="
echo ""

verify_lib() {
    local name=$1
    local lib=$2
    
    if ldconfig -p | grep -q "$lib"; then
        echo "✓ $name: FOUND"
    else
        echo "✗ $name: NOT FOUND"
    fi
}

verify_lib "HDF5" "libhdf5.so"
verify_lib "CFITSIO" "libcfitsio.so"
verify_lib "FFTW3" "libfftw3.so"
verify_lib "OpenBLAS" "libopenblas.so"
verify_lib "Bullet" "libBulletDynamics.so"

if command -v mpirun &> /dev/null; then
    echo "✓ MPI: FOUND ($(mpirun --version | head -1))"
else
    echo "✗ MPI: NOT FOUND"
fi

echo ""
echo "=== Installation Complete ==="
echo ""
echo "Next steps:"
echo "1. Run: source ~/.bashrc"
echo "2. Verify: ./verify_native_libs_linux.sh"
echo "3. Build JScience: mvn clean compile"
echo ""

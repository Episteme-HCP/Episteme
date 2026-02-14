#!/bin/bash
# JScience HPC - Native Libraries Installer for RHEL/CentOS/Fedora
# This script installs all required native libraries using dnf

set -e

echo "=== JScience HPC Native Libraries Installer (RHEL/CentOS/Fedora) ==="
echo ""

# Check if running as root
if [ "$EUID" -ne 0 ]; then 
    echo "Please run as root (use sudo)"
    exit 1
fi

# Enable EPEL repository (for RHEL/CentOS)
if [ -f /etc/redhat-release ]; then
    if grep -q "Red Hat\|CentOS" /etc/redhat-release; then
        echo "Enabling EPEL repository..."
        dnf install -y epel-release || true
    fi
fi

# Function to install package
install_package() {
    local name=$1
    shift
    local packages=("$@")
    
    echo ""
    echo "--- Installing $name ---"
    dnf install -y "${packages[@]}"
    echo "✓ $name installed"
}

# Install MPI
install_package "OpenMPI" \
    openmpi openmpi-devel

# Install HDF5
install_package "HDF5" \
    hdf5 hdf5-devel hdf5-static

# Install CFITSIO
install_package "CFITSIO" \
    cfitsio cfitsio-devel

# Install FFTW3
install_package "FFTW3" \
    fftw fftw-devel

# Install OpenBLAS
install_package "OpenBLAS" \
    openblas openblas-devel

# Install Bullet Physics
install_package "Bullet Physics" \
    bullet bullet-devel

# Install build tools
install_package "Build Tools" \
    gcc gcc-c++ make cmake git pkgconfig

# Configure MPI paths
echo ""
echo "Configuring MPI environment..."
if [ ! -f /etc/profile.d/openmpi.sh ]; then
    cat > /etc/profile.d/openmpi.sh << 'EOF'
export PATH=$PATH:/usr/lib64/openmpi/bin
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib64/openmpi/lib
EOF
    chmod +x /etc/profile.d/openmpi.sh
    echo "✓ Created /etc/profile.d/openmpi.sh"
fi

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

# MPI check (need to source profile first)
if [ -f /usr/lib64/openmpi/bin/mpirun ]; then
    echo "✓ MPI: FOUND at /usr/lib64/openmpi/bin/mpirun"
else
    echo "✗ MPI: NOT FOUND"
fi

echo ""
echo "=== Installation Complete ==="
echo ""
echo "Next steps:"
echo "1. Logout and login again (or run: source /etc/profile.d/openmpi.sh)"
echo "2. Verify: ./verify_native_libs_linux.sh"
echo "3. Build JScience: mvn clean compile"
echo ""

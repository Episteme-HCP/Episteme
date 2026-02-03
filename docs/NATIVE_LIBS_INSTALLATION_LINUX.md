# Native Libraries Installation Guide for JScience HPC - Linux

## Overview

This guide provides installation instructions for all native libraries required by JScience HPC on Linux systems (Ubuntu/Debian, RHEL/CentOS/Fedora, Arch).

---

## Quick Start

### Automated Installation (Recommended)

```bash
# Download and run the installer
curl -O https://raw.githubusercontent.com/jscience/jscience/main/install_native_libs_linux.sh
chmod +x install_native_libs_linux.sh
sudo ./install_native_libs_linux.sh
```

Or use the package manager script:

```bash
# For Ubuntu/Debian
sudo ./install_libs_apt.sh

# For RHEL/CentOS/Fedora
sudo ./install_libs_dnf.sh

# For Arch Linux
sudo ./install_libs_pacman.sh
```

---

## Manual Installation

### 1. MPI (Message Passing Interface)

#### Option A: OpenMPI (Recommended)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install -y openmpi-bin openmpi-common libopenmpi-dev
```

**RHEL/CentOS/Fedora:**
```bash
sudo dnf install -y openmpi openmpi-devel
# Add to PATH
echo 'export PATH=$PATH:/usr/lib64/openmpi/bin' >> ~/.bashrc
echo 'export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib64/openmpi/lib' >> ~/.bashrc
source ~/.bashrc
```

**Arch Linux:**
```bash
sudo pacman -S openmpi
```

**Verification:**
```bash
mpirun --version
# Should output: mpirun (Open MPI) 4.x.x
```

#### Option B: MPJ Express (Java-specific)

```bash
# Download
wget https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.tar.gz
tar -xzf mpj-v0_44.tar.gz
sudo mv mpj-v0_44 /opt/mpj

# Configure
echo 'export MPJ_HOME=/opt/mpj' >> ~/.bashrc
echo 'export PATH=$PATH:$MPJ_HOME/bin' >> ~/.bashrc
source ~/.bashrc

# Test
mpjrun.sh -np 4 -jar /opt/mpj/lib/starter.jar
```

---

### 2. HDF5 Library

**Ubuntu/Debian:**
```bash
sudo apt install -y libhdf5-dev libhdf5-serial-dev hdf5-tools
```

**RHEL/CentOS/Fedora:**
```bash
sudo dnf install -y hdf5 hdf5-devel hdf5-static
```

**Arch Linux:**
```bash
sudo pacman -S hdf5
```

**Verification:**
```bash
h5dump --version
# Should output: h5dump: Version 1.14.x
```

**Library Locations:**
- Headers: `/usr/include/hdf5/serial/` (Debian) or `/usr/include/` (others)
- Libraries: `/usr/lib/x86_64-linux-gnu/hdf5/serial/` (Debian) or `/usr/lib64/` (others)

---

### 3. CFITSIO Library

**Ubuntu/Debian:**
```bash
sudo apt install -y libcfitsio-dev libcfitsio-bin
```

**RHEL/CentOS/Fedora:**
```bash
sudo dnf install -y cfitsio cfitsio-devel
```

**Arch Linux:**
```bash
sudo pacman -S cfitsio
```

**Build from Source (if needed):**
```bash
wget http://heasarc.gsfc.nasa.gov/FTP/software/fitsio/c/cfitsio-4.3.0.tar.gz
tar -xzf cfitsio-4.3.0.tar.gz
cd cfitsio-4.3.0
./configure --prefix=/usr/local
make
sudo make install
sudo ldconfig
```

**Verification:**
```bash
pkg-config --modversion cfitsio
# Should output: 4.3.0 or similar
```

---

### 4. FFTW3 Library

**Ubuntu/Debian:**
```bash
sudo apt install -y libfftw3-dev libfftw3-double3 libfftw3-single3
```

**RHEL/CentOS/Fedora:**
```bash
sudo dnf install -y fftw fftw-devel
```

**Arch Linux:**
```bash
sudo pacman -S fftw
```

**Verification:**
```bash
pkg-config --modversion fftw3
# Should output: 3.3.10 or similar
```

**Library Locations:**
- `/usr/lib/x86_64-linux-gnu/libfftw3.so` (Debian)
- `/usr/lib64/libfftw3.so` (RHEL/Fedora)

---

### 5. OpenBLAS Library

**Ubuntu/Debian:**
```bash
sudo apt install -y libopenblas-dev libopenblas-base
```

**RHEL/CentOS/Fedora:**
```bash
sudo dnf install -y openblas openblas-devel
```

**Arch Linux:**
```bash
sudo pacman -S openblas
```

**Verification:**
```bash
pkg-config --modversion openblas
# Should output: 0.3.x
```

---

### 6. Bullet Physics Library

**Ubuntu/Debian:**
```bash
sudo apt install -y libbullet-dev libbullet-extras-dev
```

**RHEL/CentOS/Fedora:**
```bash
sudo dnf install -y bullet bullet-devel
```

**Arch Linux:**
```bash
sudo pacman -S bullet
```

**Build from Source (for custom C-API wrapper):**
```bash
# Clone repository
git clone https://github.com/bulletphysics/bullet3.git /tmp/bullet3
cd /tmp/bullet3

# Build
mkdir build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release \
         -DBUILD_SHARED_LIBS=ON \
         -DUSE_DOUBLE_PRECISION=ON \
         -DCMAKE_INSTALL_PREFIX=/usr/local
make -j$(nproc)
sudo make install
sudo ldconfig

# Create C-API wrapper (see NATIVE_LIBS_INSTALLATION.md for code)
cd /tmp/bullet3
mkdir bullet_capi && cd bullet_capi

# Copy bullet_capi.h and bullet_capi.cpp from Windows guide
# Then build:
cat > CMakeLists.txt << 'EOF'
cmake_minimum_required(VERSION 3.10)
project(bullet_capi)

set(CMAKE_CXX_STANDARD 11)

find_package(Bullet REQUIRED)

add_library(bullet_capi SHARED bullet_capi.cpp)
target_include_directories(bullet_capi PRIVATE ${BULLET_INCLUDE_DIRS})
target_link_libraries(bullet_capi ${BULLET_LIBRARIES})

install(TARGETS bullet_capi DESTINATION lib)
install(FILES bullet_capi.h DESTINATION include)
EOF

mkdir build && cd build
cmake ..
make
sudo make install
sudo ldconfig
```

**Verification:**
```bash
ldconfig -p | grep bullet
# Should show libbullet*.so entries
```

---

### 7. Java Native Access (JNA) - Optional

For easier native library loading:

```bash
# Already included in Maven dependencies, but system package can help
sudo apt install -y libjna-java  # Debian/Ubuntu
sudo dnf install -y jna          # RHEL/Fedora
sudo pacman -S java-jna          # Arch
```

---

## Environment Configuration

### Set Library Paths

Add to `~/.bashrc` or `~/.profile`:

```bash
# HDF5
export HDF5_DIR=/usr
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib/x86_64-linux-gnu/hdf5/serial

# FFTW3
export FFTW3_DIR=/usr

# OpenBLAS
export OPENBLAS_DIR=/usr

# Bullet Physics
export BULLET_DIR=/usr/local

# MPJ Express (if using)
export MPJ_HOME=/opt/mpj
export PATH=$PATH:$MPJ_HOME/bin

# Java library path (for JNI)
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/lib/jvm/default/lib/server
```

Apply changes:
```bash
source ~/.bashrc
```

---

## Verification Script

Save as `verify_native_libs_linux.sh`:

```bash
#!/bin/bash

echo "=== JScience HPC Native Libraries Verification (Linux) ==="
echo ""

# Function to check library
check_lib() {
    local name=$1
    local lib=$2
    
    if ldconfig -p | grep -q "$lib"; then
        echo "✓ $name: FOUND"
        ldconfig -p | grep "$lib" | head -1
    else
        echo "✗ $name: NOT FOUND"
    fi
    echo ""
}

# Check libraries
check_lib "HDF5" "libhdf5.so"
check_lib "CFITSIO" "libcfitsio.so"
check_lib "FFTW3" "libfftw3.so"
check_lib "OpenBLAS" "libopenblas.so"
check_lib "Bullet" "libBulletDynamics.so"

# Check MPI
echo "Checking MPI..."
if command -v mpirun &> /dev/null; then
    echo "✓ MPI: FOUND"
    mpirun --version | head -1
else
    echo "✗ MPI: NOT FOUND"
fi
echo ""

# Check MPJ Express
echo "Checking MPJ Express..."
if [ -d "$MPJ_HOME" ]; then
    echo "✓ MPJ Express: FOUND at $MPJ_HOME"
else
    echo "✗ MPJ Express: NOT FOUND (MPJ_HOME not set)"
fi
echo ""

# Check Java
echo "Checking Java..."
if command -v java &> /dev/null; then
    echo "✓ Java: FOUND"
    java -version 2>&1 | head -1
else
    echo "✗ Java: NOT FOUND"
fi
echo ""

# Check pkg-config
echo "Checking pkg-config availability..."
for pkg in hdf5 cfitsio fftw3 openblas; do
    if pkg-config --exists $pkg 2>/dev/null; then
        echo "✓ $pkg: $(pkg-config --modversion $pkg)"
    else
        echo "✗ $pkg: Not available via pkg-config"
    fi
done

echo ""
echo "=== Verification Complete ==="
```

Run with:
```bash
chmod +x verify_native_libs_linux.sh
./verify_native_libs_linux.sh
```

---

## Docker Container (Alternative)

For a reproducible environment:

```dockerfile
FROM ubuntu:22.04

# Install all dependencies
RUN apt-get update && apt-get install -y \
    openmpi-bin openmpi-common libopenmpi-dev \
    libhdf5-dev libhdf5-serial-dev hdf5-tools \
    libcfitsio-dev libcfitsio-bin \
    libfftw3-dev libfftw3-double3 \
    libopenblas-dev libopenblas-base \
    libbullet-dev \
    openjdk-21-jdk \
    maven \
    git \
    cmake \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Set environment
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

# Copy JScience project
WORKDIR /workspace
COPY . .

# Build
RUN mvn clean install -DskipTests

CMD ["/bin/bash"]
```

Build and run:
```bash
docker build -t jscience-hpc .
docker run -it --rm jscience-hpc
```

---

## Troubleshooting

### Library Not Found Errors

```bash
# Update library cache
sudo ldconfig

# Check library search paths
ldconfig -p | grep <library_name>

# Add custom library path
echo "/usr/local/lib" | sudo tee /etc/ld.so.conf.d/local.conf
sudo ldconfig
```

### Java Cannot Find Native Library

```bash
# Set java.library.path
java -Djava.library.path=/usr/lib:/usr/local/lib -jar your-app.jar

# Or in Maven:
mvn exec:java -Djava.library.path=/usr/lib:/usr/local/lib
```

### MPI Initialization Fails

```bash
# Check MPI installation
mpirun --version

# Test MPI
mpirun -np 4 hostname

# For MPJ Express
mpjrun.sh -np 4 -jar $MPJ_HOME/lib/starter.jar
```

### Permission Denied

```bash
# Add user to necessary groups
sudo usermod -aG sudo $USER
sudo usermod -aG docker $USER  # if using Docker

# Logout and login again
```

---

## Distribution-Specific Notes

### Ubuntu 20.04/22.04
- All packages available in default repositories
- HDF5 libraries in `/usr/lib/x86_64-linux-gnu/hdf5/serial/`

### RHEL 8/9, CentOS Stream, Fedora
- Enable EPEL repository for some packages:
  ```bash
  sudo dnf install epel-release
  ```
- Libraries typically in `/usr/lib64/`

### Arch Linux
- All packages in official repositories
- Use `pacman -Ss <package>` to search

### Alpine Linux (for containers)
```bash
apk add openmpi openmpi-dev hdf5 hdf5-dev fftw fftw-dev openblas openblas-dev
```

---

## Performance Tuning

### OpenMPI Configuration

Create `~/.openmpi/mca-params.conf`:
```
# Use shared memory for local communication
btl = ^tcp
btl_sm_use_knem = 1

# Increase buffer sizes
btl_openib_eager_limit = 32768
btl_openib_max_send_size = 131072
```

### FFTW Wisdom (Pre-computed Plans)

```bash
# Generate wisdom for common sizes
fftwf-wisdom -v -c -n -o ~/.fftw_wisdom \
    rof1024 rof2048 rof4096 rof8192 rof16384

# Use in Java
System.setProperty("fftw.wisdom.file", System.getProperty("user.home") + "/.fftw_wisdom");
```

---

## Summary

**Required Libraries**:
1. ✅ MPI (OpenMPI or MPJ Express)
2. ✅ HDF5 (libhdf5-dev)
3. ✅ CFITSIO (libcfitsio-dev)
4. ✅ FFTW3 (libfftw3-dev)
5. ✅ OpenBLAS (libopenblas-dev)
6. ✅ Bullet Physics (libbullet-dev)

**Installation Time**: ~10-20 minutes (with package manager)  
**Disk Space**: ~500 MB total  
**Tested On**: Ubuntu 22.04, Fedora 38, Arch Linux (2023)

---

**Last Updated**: 2026-01-30  
**Maintained By**: JScience HPC Team

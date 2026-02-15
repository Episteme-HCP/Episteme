#!/bin/bash
# JScience HPC - Native Libraries Auto-Installer
# Bash script to download and install all required native libraries
# Updated to prioritize local archives

INSTALL_DIR="$HOME/JScience-Native"
SKIP_MPI=false
SKIP_HDF5=false
SKIP_FFTW=false
SKIP_BULLET=false
SKIP_OPENBLAS=false

# Helper function to check for local archive
get_local_archive() {
    local filename=$1
    if [ -f "./$filename" ]; then
        echo "$(pwd)/$filename"
    elif [ -f "$(dirname "$0")/$filename" ]; then
        echo "$(dirname "$0")/$filename"
    else
        echo ""
    fi
}

# Parse arguments
for arg in "$@"
do
    case $arg in
        --skip-mpi)
        SKIP_MPI=true
        shift
        ;;
        --skip-hdf5)
        SKIP_HDF5=true
        shift
        ;;
        --skip-fftw)
        SKIP_FFTW=true
        shift
        ;;
        --skip-bullet)
        SKIP_BULLET=true
        shift
        ;;
        --skip-openblas)
        SKIP_OPENBLAS=true
        shift
        ;;
        --install-dir=*)
        INSTALL_DIR="${arg#*=}"
        shift
        ;;
    esac
done

echo "=== JScience HPC Native Libraries Installer ==="
echo "Installation Directory: $INSTALL_DIR"
echo ""

# Create installation directory
mkdir -p "$INSTALL_DIR"

# Function to download and extract
install_library() {
    local name=$1
    local url=$2
    local filename=$3
    local subdir=$4
    
    echo ""
    echo "--- Installing $name ---"
    
    local extract_path="$INSTALL_DIR/$subdir"
    local local_archive=$(get_local_archive "$filename")
    local archive_path=""

    if [ -n "$local_archive" ]; then
        echo "[INFO] Found local archive: $local_archive"
        archive_path="$local_archive"
    else
        # Download
        echo "Downloading from $url..."
        archive_path="/tmp/$filename"
        if command -v wget >/dev/null 2>&1; then
            wget -q --show-progress -O "$archive_path" "$url"
        elif command -v curl >/dev/null 2>&1; then
            curl -L -o "$archive_path" "$url"
        else
            echo "[ERROR] Neither wget nor curl found. Cannot download."
            return 1
        fi
        
        if [ $? -eq 0 ]; then
            echo "[OK] Downloaded"
        else
            echo "[ERROR] Download failed"
            rm -f "$archive_path"
            return 1
        fi
    fi
    
    # Extract
    mkdir -p "$extract_path"
    echo "Extracting to $extract_path..."
    
    if [[ "$filename" == *.zip ]]; then
        if command -v unzip >/dev/null 2>&1; then
            unzip -o -q "$archive_path" -d "$extract_path"
            echo "[OK] Extracted"
        else
            echo "[ERROR] unzip not found"
            return 1
        fi
    elif [[ "$filename" == *.tar.gz ]] || [[ "$filename" == *.tgz ]]; then
        tar -xzf "$archive_path" -C "$extract_path" --strip-components=1 2>/dev/null || tar -xzf "$archive_path" -C "$extract_path"
        echo "[OK] Extracted"
    fi
    
    # Cleanup download
    if [ -z "$local_archive" ]; then
        rm -f "$archive_path"
    fi
    
    return 0
}

# 1. Install MPJ Express
if [ "$SKIP_MPI" = false ]; then
    install_library "MPJ Express" \
        "https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.tar.gz/download" \
        "mpj-v0_44.tar.gz" \
        "MPJ"
        
    # MPJ specific: set MPJ_HOME in shell profile?
    echo "[INFO] Please add the following to your .bashrc or .zshrc:"
    echo "export MPJ_HOME=$INSTALL_DIR/MPJ"
    echo "export PATH=\$PATH:\$MPJ_HOME/bin"
fi

# 2. Install HDF5
if [ "$SKIP_HDF5" = false ]; then
    echo ""
    echo "--- Installing HDF5 ---"
    # Linux distributions usually provide HDF5 via package manager
    echo "[INFO] On Linux, it is recommended to install HDF5 via package manager:"
    echo "  Ubuntu/Debian: sudo apt-get install libhdf5-dev"
    echo "  RHEL/CentOS: sudo dnf install hdf5-devel"
    echo "Attempting to download pre-built binaries (if available) or generic source..."
    
    install_library "HDF5" \
        "https://support.hdfgroup.org/ftp/HDF5/releases/hdf5-1.14/hdf5-1.14.3/src/hdf5-1.14.3.tar.gz" \
        "hdf5-1.14.3.tar.gz" \
        "HDF5"
        
    echo "[WARN] HDF5 installed from source/archive. You may need to compile it or add to LD_LIBRARY_PATH."
    echo "export HDF5_DIR=$INSTALL_DIR/HDF5"
    echo "export LD_LIBRARY_PATH=\$LD_LIBRARY_PATH:\$HDF5_DIR/lib"
fi

# 3. Install FFTW3
if [ "$SKIP_FFTW" = false ]; then
    install_library "FFTW3" \
        "http://www.fftw.org/fftw-3.3.10.tar.gz" \
        "fftw-3.3.10.tar.gz" \
        "FFTW3"
        
    echo "[ABOVE] FFTW3 source installed. Compilation required:"
    echo "  cd $INSTALL_DIR/FFTW3"
    echo "  ./configure --enable-shared --enable-threads --enable-sse2 --enable-avx"
    echo "  make -j4 && sudo make install"
fi

# 4. Install Bullet Physics
if [ "$SKIP_BULLET" = false ]; then
    echo ""
    echo "--- Installing Bullet Physics ---"
    if command -v git >/dev/null 2>&1; then
        BULLET_PATH="$INSTALL_DIR/bullet3"
        if [ ! -d "$BULLET_PATH" ]; then
            echo "Cloning Bullet3..."
            git clone https://github.com/bulletphysics/bullet3.git "$BULLET_PATH"
            echo "[OK] Cloned Bullet3"
        else
            echo "[OK] Bullet3 already cloned"
        fi
        echo "[INFO] Build Bullet3 with CMake:"
        echo "  cd $BULLET_PATH && mkdir build && cd build && cmake .. && make -j4"
    fi
fi

# 5. Install OpenBLAS
if [ "$SKIP_OPENBLAS" = false ]; then
    install_library "OpenBLAS" \
        "https://github.com/xianyi/OpenBLAS/releases/download/v0.3.25/OpenBLAS-0.3.25.tar.gz" \
        "OpenBLAS-0.3.25.tar.gz" \
        "OpenBLAS"
        
    echo "[INFO] OpenBLAS source installed. Compilation required:"
    echo "  cd $INSTALL_DIR/OpenBLAS"
    echo "  make -j4 && sudo make install"
fi

echo ""
echo "=== Installation Summary ==="
echo "Check output above for instructions on compiling sources (FFTW, OpenBLAS, Bullet)."
echo "Ensure generated libraries are in your LD_LIBRARY_PATH."
echo ""
echo "[OK] Script complete."

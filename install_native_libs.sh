#!/bin/bash
# JScience HPC - Native Libraries Auto-Installer
# Bash script to download and install all required native libraries

INSTALL_DIR="$HOME/JScience-Native"
SKIP_MPI=false
SKIP_HDF5=false
SKIP_FFTW=false
SKIP_BULLET=false
SKIP_OPENBLAS=false

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

echo -e "\033[0;36m=== JScience HPC Native Libraries Installer ===\033[0m"
echo -e "\033[0;33mInstallation Directory: $INSTALL_DIR\033[0m"
echo ""

# Create installation directory
if [ ! -d "$INSTALL_DIR" ]; then
    mkdir -p "$INSTALL_DIR"
    echo -e "\033[0;32m✓ Created installation directory\033[0m"
fi

# Function to download and extract
install_library() {
    NAME=$1
    URL=$2
    FILENAME=$3
    SUBDIR=$4
    
    echo -e "\n\033[0;36m--- Installing $NAME ---\033[0m"
    
    DOWNLOAD_PATH="/tmp/$FILENAME"
    EXTRACT_PATH="$INSTALL_DIR/$SUBDIR"
    mkdir -p "$EXTRACT_PATH"
    
    echo -e "\033[0;33mDownloading from $URL...\033[0m"
    if command -v wget &> /dev/null; then
        wget -q -O "$DOWNLOAD_PATH" "$URL"
    elif command -v curl &> /dev/null; then
        curl -L -s -o "$DOWNLOAD_PATH" "$URL"
    else
        echo -e "\033[0;31m✗ Error: Neither wget nor curl found.\033[0m"
        return 1
    fi
    echo -e "\033[0;32m✓ Downloaded\033[0m"
    
    echo -e "\033[0;33mExtracting to $EXTRACT_PATH...\033[0m"
    if [[ "$FILENAME" == *.zip ]]; then
        unzip -q -o "$DOWNLOAD_PATH" -d "$EXTRACT_PATH"
    elif [[ "$FILENAME" == *.tar.gz ]] || [[ "$FILENAME" == *.tgz ]]; then
        tar -xzf "$DOWNLOAD_PATH" -C "$EXTRACT_PATH"
    fi
    echo -e "\033[0;32m✓ Extracted\033[0m"
    
    rm -f "$DOWNLOAD_PATH"
    echo "$EXTRACT_PATH"
}

# 1. Install MPJ Express
if [ "$SKIP_MPI" = false ]; then
    MPJ_PATH=$(install_library "MPJ Express" "https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.tar.gz/download" "mpj-v0_44.tar.gz" "MPJ")
    
    if [ -n "$MPJ_PATH" ]; then
        export MPJ_HOME="$MPJ_PATH"
        echo -e "\033[0;32m✓ Set MPJ_HOME=$MPJ_PATH\033[0m"
    fi
fi

# 2. Install HDF5
if [ "$SKIP_HDF5" = false ]; then
    # Linux binaries for HDF5
    HDF5_URL="https://support.hdfgroup.org/ftp/HDF5/releases/hdf5-1.14/hdf5-1.14.3/bin/unix/hdf5-1.14.3-ubuntu-2204.tar.gz"
    # Note: URL might need adjustment based on distro, using Ubuntu 22.04 as generically compatible base
    
    HDF5_PATH=$(install_library "HDF5" "$HDF5_URL" "hdf5-1.14.3.tar.gz" "HDF5")
    
    if [ -n "$HDF5_PATH" ]; then
        export HDF5_DIR="$HDF5_PATH"
        export PATH="$HDF5_PATH/bin:$PATH"
        export LD_LIBRARY_PATH="$HDF5_PATH/lib:$LD_LIBRARY_PATH"
        echo -e "\033[0;32m✓ Added HDF5 to PATH and LD_LIBRARY_PATH\033[0m"
    fi
fi

# 3. Install FFTW3
if [ "$SKIP_FFTW" = false ]; then
    FFTW_PATH=$(install_library "FFTW3" "http://www.fftw.org/fftw-3.3.10.tar.gz" "fftw-3.3.10.tar.gz" "FFTW3")
    
    if [ -n "$FFTW_PATH" ]; then
        echo -e "\033[0;33mBuilding FFTW3 from source (Linux/Mac)...\033[0m"
        cd "$FFTW_PATH/fftw-3.3.10" || cd "$FFTW_PATH"
        ./configure --enable-shared --enable-threads --enable-float
        make -j$(nproc)
        make install PREFIX="$INSTALL_DIR/FFTW3_build"
        
        export LD_LIBRARY_PATH="$INSTALL_DIR/FFTW3_build/lib:$LD_LIBRARY_PATH"
        echo -e "\033[0;32m✓ Built and added FFTW3 to LD_LIBRARY_PATH\033[0m"
    fi
fi

# 4. Install Bullet Physics
if [ "$SKIP_BULLET" = false ]; then
    echo -e "\n\033[0;36m--- Installing Bullet Physics ---\033[0m"
    if command -v git &> /dev/null && command -v cmake &> /dev/null; then
        BULLET_PATH="$INSTALL_DIR/bullet3"
        if [ ! -d "$BULLET_PATH" ]; then
            git clone https://github.com/bulletphysics/bullet3.git "$BULLET_PATH"
            echo -e "\033[0;32m✓ Cloned Bullet3\033[0m"
            
            mkdir -p "$BULLET_PATH/build"
            cd "$BULLET_PATH/build"
            cmake .. -DBUILD_SHARED_LIBS=ON
            make -j$(nproc)
            echo -e "\033[0;32m✓ Built Bullet3\033[0m"
        else
            echo -e "\033[0;32m✓ Bullet3 already cloned\033[0m"
        fi
    else
         echo -e "\033[0;33mSkipping Bullet (git or cmake not found)\033[0m"
    fi
fi

# 5. Install OpenBLAS
if [ "$SKIP_OPENBLAS" = false ]; then
    # OpenBLAS often available via apt/yum, but here is a binary/source attempt
    OPENBLAS_URL="https://github.com/xianyi/OpenBLAS/releases/download/v0.3.25/OpenBLAS-0.3.25.tar.gz"
    OPENBLAS_PATH=$(install_library "OpenBLAS" "$OPENBLAS_URL" "OpenBLAS-0.3.25.tar.gz" "OpenBLAS")
    
    if [ -n "$OPENBLAS_PATH" ]; then
         echo -e "\033[0;33mBuilding OpenBLAS from source...\033[0m"
         cd "$OPENBLAS_PATH"
         make -j$(nproc)
         make install PREFIX="$INSTALL_DIR/OpenBLAS_build"
         
         export LD_LIBRARY_PATH="$INSTALL_DIR/OpenBLAS_build/lib:$LD_LIBRARY_PATH"
         echo -e "\033[0;32m✓ Built and added OpenBLAS to LD_LIBRARY_PATH\033[0m"
    fi
fi


echo -e "\n\033[0;36m=== Installation Summary ===\033[0m"
echo "Installation directory: $INSTALL_DIR"
echo -e "\n\033[0;33mNext steps:\033[0m"
echo "1. Run 'source ~/.bashrc' (or equivalent) to apply changes if any were persisted (script only exports for this session)"
echo "2. Add the exported variables to your shell profile manually."

#!/usr/bin/env bash
# Episteme HPC - Native Libraries Verification Script (Linux/macOS)
# Checks environment variables, local paths, and path visibility

set -euo pipefail

CYAN='\033[0;36m'
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
GRAY='\033[0;90m'
NC='\033[0m' # No Color

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LIBS_DIR="$SCRIPT_DIR/libs"

echo -e "${CYAN}=== Episteme Native Library Verification ===${NC}"
echo ""
echo -e "${GRAY}Script directory: $SCRIPT_DIR${NC}"
echo -e "${GRAY}Libs directory:   $LIBS_DIR${NC}"
echo ""

FOUND=0
TOTAL=0

# check_lib <Name> <EnvVar> <LocalDir> <CheckFile> <Type>
check_lib() {
    local name="$1"
    local envvar="$2"
    local localdir="$3"
    local checkfile="$4"
    local type="$5"

    TOTAL=$((TOTAL + 1))

    local status="MISSING"
    local details=""

    # 1. Check Environment Variable
    if [ -n "$envvar" ]; then
        local envval="${!envvar:-}"
        if [ -n "$envval" ]; then
            if [ -e "$envval" ]; then
                details="EnvVar ${envvar}=${envval}. "
                if [ -e "$envval/$checkfile" ]; then
                    status="OK (Env)"
                fi
            else
                details="EnvVar set but path invalid (${envval}). "
            fi
        fi
    fi

    # 2. Check local libs/ directory
    if [ "$status" = "MISSING" ]; then
        local localpath="$LIBS_DIR/$localdir"
        if [ -d "$localpath" ]; then
            if [ -e "$localpath/$checkfile" ]; then
                status="OK (Local)"
                details="${details}Found at $localpath/$checkfile. "
            else
                details="${details}Dir exists ($localpath) but check file missing. "
            fi
        fi
    fi

    # 3. Check in system PATH (for native libs/shared objects)
    if [ "$status" = "MISSING" ] && [ "$type" = "native" ]; then
        local filename
        filename="$(basename "$checkfile")"
        # Also check .so variants
        local so_name="${filename%.dll}"
        so_name="lib${so_name#lib}.so"
        local dylib_name="${filename%.dll}"
        dylib_name="lib${dylib_name#lib}.dylib"

        if command -v "$filename" &>/dev/null; then
            status="OK (PATH)"
        elif ldconfig -p 2>/dev/null | grep -qi "$so_name"; then
            status="OK (ldconfig)"
        fi
    fi

    # Print result
    if [[ "$status" == OK* ]]; then
        FOUND=$((FOUND + 1))
        echo -e "Checking ${name}... ${GREEN}[${status}]${NC}"
    else
        echo -e "Checking ${name}... ${RED}[MISSING]${NC}"
    fi
    echo -e "  ${GRAY}Type: ${type}${NC}"
    if [ -n "$details" ]; then
        echo -e "  ${GRAY}> ${details}${NC}"
    fi
}

# ── Library checks ──────────────────────────────────────────────

# Determine platform-specific check files
OS="$(uname -s)"
case "$OS" in
    Linux*)
        MPJ_CHECK="lib/mpj.jar"
        HDF5_CHECK="lib/libhdf5.so"
        FFTW_CHECK="libfftw3.so"
        BLAS_CHECK="lib/libopenblas.so"
        BULLET_CHECK="libbulletc.so"
        ;;
    Darwin*)
        MPJ_CHECK="lib/mpj.jar"
        HDF5_CHECK="lib/libhdf5.dylib"
        FFTW_CHECK="libfftw3.dylib"
        BLAS_CHECK="lib/libopenblas.dylib"
        BULLET_CHECK="libbulletc.dylib"
        ;;
    *)
        # Fallback to Windows names (MSYS/Cygwin)
        MPJ_CHECK="lib/mpj.jar"
        HDF5_CHECK="bin/hdf5.dll"
        FFTW_CHECK="libfftw3-3.dll"
        BLAS_CHECK="bin/libopenblas.dll"
        BULLET_CHECK="libbulletc.dll"
        ;;
esac

#         Name          EnvVar          LocalDir                  CheckFile              Type
check_lib "MPJ Express" "MPJ_HOME"      "MPJ"                     "$MPJ_CHECK"           "java"
check_lib "HDF5"        "HDF5_DIR"      "HDF5"                    "$HDF5_CHECK"          "native"
check_lib "FFTW3"       "FFTW3_DIR"     "FFTW3"                   "$FFTW_CHECK"          "native"
check_lib "OpenBLAS"    "OpenBLAS_DIR"  "OpenBLAS"                "$BLAS_CHECK"          "native"
check_lib "Bullet3"     "BULLET_HOME"   "Bullet3DLL"              "$BULLET_CHECK"        "native"
check_lib "TarsosDSP"   ""              "Tarsos/Tarsos-master"    "lib/TarsosDSP-2.4.jar" "java"
check_lib "oneDNN"      "DNNL_DIR"      "oneDNN/oneDNN-main"      "CMakeLists.txt"       "source"

# ── Summary ─────────────────────────────────────────────────────

echo ""
if [ "$FOUND" -eq "$TOTAL" ]; then
    echo -e "${GREEN}=== Summary: $FOUND / $TOTAL libraries found ===${NC}"
else
    echo -e "${YELLOW}=== Summary: $FOUND / $TOTAL libraries found ===${NC}"
fi
echo ""
echo -e "${GRAY}Notes:${NC}"
echo -e "${GRAY}  - 'native' libraries must be in LD_LIBRARY_PATH or pointed to by env vars.${NC}"
echo -e "${GRAY}  - 'java' libraries are resolved via Maven or classpath.${NC}"
echo -e "${GRAY}  - 'source' libraries must be compiled first (see their README).${NC}"

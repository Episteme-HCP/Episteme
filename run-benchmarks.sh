#!/bin/bash
# Episteme Benchmarks Launcher

APP_CLASS="org.episteme.benchmarks.ui.Launcher"
JAR_PATH="episteme-benchmarks/target/episteme-benchmarks.jar"
LIB_DIR="launchers/lib"
MODULE_PATH="episteme-benchmarks/target/classes:episteme-core/target/classes:episteme-natural/target/classes:episteme-social/target/classes:episteme-native/target/classes"

USE_SHADED=false

# Parse arguments
EXPORT_FILE=""
GENERATE_PDF=false

for arg in "$@"
do
    if [ "$arg" == "--cli" ]; then
        APP_CLASS="org.episteme.benchmarks.cli.BenchmarkCLI"
    fi
    if [ "$arg" == "--shaded" ] || [ "$arg" == "-jar" ]; then
        USE_SHADED=true
    fi
    if [[ "$arg" == --export-file=* ]]; then
        EXPORT_FILE="${arg#*=}"
    fi
    if [ "$arg" == "--pdf" ]; then
        GENERATE_PDF=true
    fi
done

# If PDF requested but no export file, set a default
if [ "$GENERATE_PDF" = true ] && [ -z "$EXPORT_FILE" ]; then
    EXPORT_FILE="benchmark-results.json"
    # Append the default export file to arguments if not already present
    set -- "$@" "--export-file=$EXPORT_FILE"
fi

# --- Environment Setup ---
export NATIVE_ROOT="/opt/episteme-native"

# --- Python (Qiskit) Integration ---
if [ -z "$EPISTEME_PYTHON" ]; then
    # Defaulting to common installation path, can be overridden by user
    export EPISTEME_PYTHON="/usr/bin/python3"
fi

# --- CUDA Setup ---
if [ -z "$CUDA_PATH" ]; then
    export CUDA_PATH="/usr/local/cuda"
fi
export PATH="$CUDA_PATH/bin:$PATH"
export LD_LIBRARY_PATH="$CUDA_PATH/lib64:$LD_LIBRARY_PATH"

# Native Library Path Setup
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
LIBS_DIR="${SCRIPT_DIR}/libs"

# Project libs directory (ODE, QuEST, oneDNN, lz4, etc.)
if [ -d "$LIBS_DIR" ]; then
    echo "[INFO] Adding libs/ to library path..."
    export LD_LIBRARY_PATH="${LIBS_DIR}:${LD_LIBRARY_PATH}"
    export DYLD_LIBRARY_PATH="${LIBS_DIR}:${DYLD_LIBRARY_PATH}"
fi

# Standard system paths
if [ -d "/usr/local/lib" ]; then
    export LD_LIBRARY_PATH="/usr/local/lib:$LD_LIBRARY_PATH"
fi

# VLC (Linux)
if [ -d "/usr/lib/vlc" ]; then
    echo "[INFO] Adding VLC to library path..."
    export LD_LIBRARY_PATH="/usr/lib/vlc:$LD_LIBRARY_PATH"
    export VLC_PLUGIN_PATH="/usr/lib/vlc/plugins"
fi
# VLC (macOS via Homebrew)
if [ -d "/Applications/VLC.app/Contents/MacOS/lib" ]; then
    echo "[INFO] Adding VLC (macOS) to library path..."
    export DYLD_LIBRARY_PATH="/Applications/VLC.app/Contents/MacOS/lib:$DYLD_LIBRARY_PATH"
    export VLC_PLUGIN_PATH="/Applications/VLC.app/Contents/MacOS/plugins"
fi

echo "=========================================="
echo "Running Episteme Benchmarks"
echo "=========================================="

if [ "$USE_SHADED" = true ]; then
    if [ -f "$JAR_PATH" ]; then
        echo "[INFO] Running from Shaded JAR: $JAR_PATH"
        java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar "$JAR_PATH" "$@"
    else
        echo "[ERROR] Shaded JAR not found at $JAR_PATH. Run 'mvn package' first."
        exit 1
    fi
elif [ -f "server.jar" ]; then
    echo "[INFO] Running from Pre-Compiled Docker Environment (server.jar)"
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "benchmarks.jar:server.jar:lib/*" "${APP_CLASS}" "$@"
else
    echo "[INFO] Running latest compiled classes - Dev Mode. Use --shaded to force JAR."
    if [ ! -d "episteme-benchmarks/target/classes" ]; then
        echo "[INFO] Classes not found, building module..."
        mvn compile -pl episteme-benchmarks -am -DskipTests
    fi
    DEPENDENCY_DIR="episteme-benchmarks/target/lib"
    if [ ! -d "$DEPENDENCY_DIR" ]; then
        echo "[INFO] Dependencies not found in target, copying..."
        mvn dependency:copy-dependencies -pl episteme-benchmarks -DoutputDirectory=target/lib -DincludeScope=runtime -DskipTests
    fi
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "${MODULE_PATH}:${DEPENDENCY_DIR}/*:${LIB_DIR}/*" "${APP_CLASS}" "$@"
fi

# --- Post-Processing: PDF Generation ---
if [ "$GENERATE_PDF" = true ]; then
    echo ""
    echo "[INFO] Generating PDF Report from $EXPORT_FILE..."
    if command -v python3 &>/dev/null; then
        python3 plot_benchmarks.py "$EXPORT_FILE"
    elif command -v python &>/dev/null; then
        python plot_benchmarks.py "$EXPORT_FILE"
    else
        echo "[WARNING] Python not found. Skipping PDF generation."
    fi
fi

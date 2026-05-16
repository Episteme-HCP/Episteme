#!/bin/bash
# Episteme Centralized Environment Setup
# Sourced by other scripts to standardize library paths and variables.

if [ "$SETUP_COMPLETE" = "true" ]; then
    return 0
fi

echo "[INFO] Initializing Episteme Environment..."

# Determine project root
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
LIBS_DIR="${PROJECT_ROOT}/libs"

# --- Java Version Setup (JDK 25 preferred) ---
check_java_version() {
    if [ -n "$1" ] && [ -x "$1/bin/java" ]; then
        local ver=$("$1/bin/java" -version 2>&1 | grep "version" | awk '{print $3}' | tr -d '"')
        local major=$(echo $ver | cut -d'.' -f1)
        if [ "$major" -ge 21 ]; then
            return 0
        fi
    fi
    return 1
}

if check_java_version "$JAVA_HOME"; then
    echo "[INFO] Using existing compatible JAVA_HOME: $JAVA_HOME"
else
    echo "[WARNING] No compatible JDK (21+) found in JAVA_HOME. Searching..."
    # Try to find JDK 25 in common locations
    for loc in "/usr/lib/jvm/java-25-openjdk" "/usr/lib/jvm/jdk-25" "/Library/Java/JavaVirtualMachines/jdk-25.jdk/Contents/Home"; do
        if check_java_version "$loc"; then
            export JAVA_HOME="$loc"
            echo "[INFO] Auto-detected compatible JDK at: $JAVA_HOME"
            break
        fi
    done
fi

if ! check_java_version "$JAVA_HOME"; then
    echo "[ERROR] No compatible JDK (21 or 25+) was found."
    return 1
fi

export PATH="$JAVA_HOME/bin:$PATH"

# --- Maven Options (JVM Args for FFM and Vector API) ---
export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

# --- Native Libraries Setup ---

# Standard Native Library Root (can be overridden)
if [ -z "$NATIVE_ROOT" ]; then
    export NATIVE_ROOT="/opt/episteme-native"
fi

# Add Episteme project libs
if [ -d "$LIBS_DIR" ]; then
    echo "[INFO] Adding project libs to library path: $LIBS_DIR"
    export LD_LIBRARY_PATH="${LIBS_DIR}:${LD_LIBRARY_PATH}"
    export DYLD_LIBRARY_PATH="${LIBS_DIR}:${DYLD_LIBRARY_PATH}"
fi

# CUDA Setup
if [ -z "$CUDA_PATH" ]; then
    export CUDA_PATH="/usr/local/cuda"
fi
if [ -d "$CUDA_PATH" ]; then
    export PATH="$CUDA_PATH/bin:$PATH"
    export LD_LIBRARY_PATH="$CUDA_PATH/lib64:$LD_LIBRARY_PATH"
fi

# VLC Integration
if [ -d "/usr/lib/vlc" ]; then
    export LD_LIBRARY_PATH="/usr/lib/vlc:$LD_LIBRARY_PATH"
    export VLC_PLUGIN_PATH="/usr/lib/vlc/plugins"
fi
if [ -d "/Applications/VLC.app/Contents/MacOS/lib" ]; then
    export DYLD_LIBRARY_PATH="/Applications/VLC.app/Contents/MacOS/lib:$DYLD_LIBRARY_PATH"
    export VLC_PLUGIN_PATH="/Applications/VLC.app/Contents/MacOS/plugins"
fi

# Python (Qiskit/ML) Integration
if [ -z "$EPISTEME_PYTHON" ]; then
    export EPISTEME_PYTHON="/usr/bin/python3"
fi

export SETUP_COMPLETE=true
echo "[INFO] Episteme Environment Setup Complete."
echo ""

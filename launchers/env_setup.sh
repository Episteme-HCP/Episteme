#!/bin/bash
# Episteme Centralized Environment Setup
# Sourced by other scripts to standardize library paths and variables.

# Standard Native Library Root (can be overridden)
if [ -z "$NATIVE_ROOT" ]; then
    export NATIVE_ROOT="/opt/episteme-native"
fi

# Determine script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LIBS_DIR="${SCRIPT_DIR}/../libs"

# Add Episteme project libs
if [ -d "$LIBS_DIR" ]; then
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

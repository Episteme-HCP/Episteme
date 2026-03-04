#!/bin/bash
# Episteme Diagnostic Tool for Linux
# Identifies native library and environment issues.

echo "=========================================="
echo "Episteme Diagnostic Tool (Linux)"
echo "=========================================="

# --- Environment Info ---
echo "[INFO] OS: $(uname -a)"
echo "[INFO] Java: $(java -version 2>&1 | head -n 1)"
echo "[INFO] Python: ${EPISTEME_PYTHON:-$(which python3)}"
${EPISTEME_PYTHON:-python3} --version 2>&1

# --- Library Check ---
LIBS_DIR="$(cd "$(dirname "$0")" && pwd)/libs"
echo "[INFO] Checking libs directory: $LIBS_DIR"
if [ -d "$LIBS_DIR" ]; then
    ls -lh "$LIBS_DIR"/*.so 2>/dev/null
else
    echo "[WARN] libs directory missing!"
fi

# --- CUDA Check ---
if [ -z "$CUDA_PATH" ]; then
    export CUDA_PATH="/usr/local/cuda"
fi
echo "[INFO] CUDA_PATH: $CUDA_PATH"
if [ -d "$CUDA_PATH" ]; then
    echo "[INFO] nvcc: $($CUDA_PATH/bin/nvcc --version 2>&1 | grep release)"
else
    echo "[WARN] CUDA_PATH not found."
fi

# --- NVIDIA-SMI Check ---
echo "[INFO] Checking NVIDIA GPU visibility..."
if command -v nvidia-smi &>/dev/null; then
    nvidia-smi -L
else
    echo "[WARN] nvidia-smi not found. GPU may not be visible to container."
fi

# --- Dependency Check (ldd) ---
echo "[INFO] Checking library dependencies (ldd)..."
for lib in /usr/lib/x86_64-linux-gnu/libOpenCL.so /usr/lib/x86_64-linux-gnu/libhdf5.so /usr/local/cuda/lib64/libcudart.so; do
    if [ -f "$lib" ]; then
        echo "Dependencies for $lib:"
        ldd "$lib" | grep "not found" || echo "  All dependencies satisfied."
    else
        echo "[WARN] Library $lib not found for ldd check."
    fi
done

# --- Run Java Diagnostics ---
echo "[INFO] Running Java Diagnostic Class..."
if [ -f "server.jar" ]; then
    # Docker Container Execution
    java -cp "benchmarks.jar:server.jar:lib/*" org.episteme.benchmarks.BackendDiagnostic "$@"
else
    # Local Development Execution
    java -cp "episteme-benchmarks/target/classes:episteme-core/target/classes:episteme-native/target/classes:episteme-benchmarks/target/lib/*" \
         org.episteme.benchmarks.BackendDiagnostic "$@"
fi

echo "=========================================="
echo "Diagnostic Complete."

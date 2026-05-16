#!/bin/bash
source "$(dirname "$0")/scripts/setup/env_setup.sh"

# Episteme Benchmarks Launcher

APP_CLASS="org.episteme.benchmarks.ui.Launcher"
JAR_PATH="episteme-benchmarks/target/episteme-benchmarks.jar"
LIB_DIR="launchers/lib"
MODULE_PATH="episteme-benchmarks/target/classes:episteme-core/target/classes:episteme-natural/target/classes:episteme-social/target/classes:episteme-native/target/classes:episteme-database/target/classes:episteme-jni/target/classes"

USE_SHADED=false

# Parse arguments
EXPORT_FILE=""
GENERATE_PDF=false

RUN_DIAGNOSTIC=false

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
    if [ "$arg" == "--diagnostic" ]; then
        RUN_DIAGNOSTIC=true
    fi
done

if [ "$RUN_DIAGNOSTIC" = true ]; then
    if [ -f "./run_diagnostic.sh" ]; then
        chmod +x ./run_diagnostic.sh
        ./run_diagnostic.sh
    else
        echo "[WARN] run_diagnostic.sh not found."
    fi
fi

# If PDF requested but no export file, Java CLI logic handles timestamp generation automatically 
# (No need to append static name).

# --- Environment Setup ---
if [ -f "./launchers/env_setup.sh" ]; then
    source "./launchers/env_setup.sh"
else
    echo "[WARN] env_setup.sh not found, using legacy path logic."
    export NATIVE_ROOT="/opt/episteme-native"
    # --- Python (Qiskit) Integration ---
    if [ -z "$EPISTEME_PYTHON" ]; then
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

    if [ -d "$LIBS_DIR" ]; then
        export LD_LIBRARY_PATH="${LIBS_DIR}:${LD_LIBRARY_PATH}"
        export DYLD_LIBRARY_PATH="${LIBS_DIR}:${DYLD_LIBRARY_PATH}"
    fi
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
elif [ -f "/app/benchmarks.jar" ] && [ -f "/app/server.jar" ]; then
    echo "[INFO] Running from Pre-Compiled Docker Environment (/app/benchmarks.jar)"
    java -Xlog:library=info -Djava.awt.headless=true -verbose:jni -XshowSettings:properties --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "benchmarks.jar:server.jar:lib/*:libs/*" "${APP_CLASS}" "$@"
else
    echo "[INFO] Running latest compiled classes - Dev Mode. Use --shaded to force JAR."
    if [ ! -d "episteme-benchmarks/target/classes" ]; then
        echo "[INFO] Classes not found, building module..."
        mvn compile -pl episteme-benchmarks -am -DskipTests -Pheadless
    fi
    DEPENDENCY_DIR="episteme-benchmarks/target/lib"
    if [ ! -d "$DEPENDENCY_DIR" ]; then
        echo "[INFO] Dependencies not found in target, copying..."
        mvn dependency:copy-dependencies -pl episteme-benchmarks -am -DoutputDirectory=target/lib -DincludeScope=runtime -DskipTests -DexcludeGroupIds=org.episteme
    fi
    java -XshowSettings:properties -Djava.awt.headless=true -verbose:jni --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "${MODULE_PATH}:${DEPENDENCY_DIR}/*:${LIB_DIR}/*" "${APP_CLASS}" "$@"
fi


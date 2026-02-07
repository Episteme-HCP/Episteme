#!/bin/bash
# Robust Benchmark Runner for JScience

APP_CLASS="org.jscience.benchmarks.benchmark.BenchmarkRunner"
JAR_PATH="jscience-benchmarks/target/jscience-benchmarks.jar"
LIB_DIR="launchers/lib"
MODULE_PATH="jscience-benchmarks/target/classes:jscience-core/target/classes:jscience-natural/target/classes:jscience-social/target/classes"

echo "=========================================="
echo "Running JScience Benchmarks"
echo "=========================================="

# --- Native Libraries Setup (Linux/Mac) ---
# Assuming standard paths or user-defined LD_LIBRARY_PATH
# For custom installations in /opt/jscience or similar
if [ -d "/usr/local/lib" ]; then
    export LD_LIBRARY_PATH="/usr/local/lib:$LD_LIBRARY_PATH"
fi

if [ -f "$JAR_PATH" ]; then
    echo "[INFO] Found shaded JAR, launching..."
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar "$JAR_PATH" "$@"
else
    echo "[INFO] Shaded JAR not found, falling back to classes..."
    if [ ! -d "jscience-benchmarks/target/classes" ]; then
        echo "[INFO] Classes not found, building module..."
        mvn compile -pl jscience-benchmarks -am -DskipTests
    fi
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "${MODULE_PATH}:${LIB_DIR}/*" "${APP_CLASS}" "$@"
fi

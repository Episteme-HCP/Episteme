#!/bin/bash
# JScience Benchmarks Launcher

APP_CLASS="org.jscience.benchmarks.ui.JScienceBenchmarkingApp"
JAR_PATH="jscience-benchmarks/target/jscience-benchmarks.jar"
LIB_DIR="launchers/lib"
MODULE_PATH="jscience-benchmarks/target/classes:jscience-core/target/classes:jscience-natural/target/classes:jscience-social/target/classes:jscience-native/target/classes"

USE_SHADED=false

# Parse arguments
for arg in "$@"
do
    if [ "$arg" == "--cli" ]; then
        APP_CLASS="org.jscience.benchmarks.cli.BenchmarkCLI"
    fi
    if [ "$arg" == "--shaded" ] || [ "$arg" == "-jar" ]; then
        USE_SHADED=true
    fi
done

# Native Library Path Setup
# Assuming standard paths or user-defined LD_LIBRARY_PATH
if [ -d "/usr/local/lib" ]; then
    export LD_LIBRARY_PATH="/usr/local/lib:$LD_LIBRARY_PATH"
fi

echo "=========================================="
echo "Running JScience Benchmarks"
echo "=========================================="

if [ "$USE_SHADED" = true ]; then
    if [ -f "$JAR_PATH" ]; then
        echo "[INFO] Running from Shaded JAR: $JAR_PATH"
        java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -jar "$JAR_PATH" "$@"
    else
        echo "[ERROR] Shaded JAR not found at $JAR_PATH. Run 'mvn package' first."
        exit 1
    fi
else
    echo "[INFO] Running latest compiled classes - Dev Mode. Use --shaded to force JAR."
    if [ ! -d "jscience-benchmarks/target/classes" ]; then
        echo "[INFO] Classes not found, building module..."
        mvn compile -pl jscience-benchmarks -am -DskipTests
    fi
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "${MODULE_PATH}:${LIB_DIR}/*" "${APP_CLASS}" "$@"
fi

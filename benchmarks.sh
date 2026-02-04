#!/bin/bash
JAR_PATH="jscience-benchmarks/target/jscience-benchmarks.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "[INFO] Benchmark JAR not found. Building..."
    mvn package -pl jscience-benchmarks -DskipTests
fi

echo ""
echo "[INFO] Starting JScience Benchmarking Suite..."
java --add-modules jdk.incubator.vector -jar "$JAR_PATH" "$@"

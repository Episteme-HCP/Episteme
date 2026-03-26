#!/bin/bash
echo "=========================================="
echo "Running Numerical Correctness Report"
echo "=========================================="

export MAVEN_OPTS="--add-modules jdk.incubator.vector --enable-preview --enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

echo "[INFO] Executing HighPrecisionCorrectnessTest..."
mvn clean test -pl episteme-benchmarks -am -Dtest=HighPrecisionCorrectnessTest -DskipTests=false

echo ""
echo "Correctness report generated in docs/benchmark-results/"
